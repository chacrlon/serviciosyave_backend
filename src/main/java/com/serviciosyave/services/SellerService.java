package com.serviciosyave.services;

import com.serviciosyave.entities.Seller;
import com.serviciosyave.repositories.SellerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;

    @Autowired
    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    // Crear o actualizar perfil
    public Seller createOrUpdateSeller(Seller seller) {
        // Si el seller no tiene fecha de creación, se establece la fecha actual
        if (seller.getCreatedAt() == null) {
            seller.setCreatedAt(LocalDateTime.now());
        }
        return sellerRepository.save(seller);
    }

    // Obtener por userId
    public Optional<Seller> getSellerByUserId(Long userId) {
        return sellerRepository.findByUserId(userId);
    }

    // Actualizar por userId
// Actualizar por userId
    public Seller updateSellerByUserId(Long userId, Seller updatedSeller) {
        return sellerRepository.findByUserId(userId)
                .map(existingSeller -> {
                    // Copiar propiedades del seller actualizado al existente, excluyendo "id" y "userId"
                    BeanUtils.copyProperties(existingSeller, updatedSeller, "id", "userId", "createdAt");
                    return sellerRepository.save(existingSeller);
                })
                .orElseGet(() -> {
                    // Si no existe un seller con ese userId, se crea uno nuevo
                    updatedSeller.setId(null); // Asegurar que sea una entidad nueva
                    updatedSeller.setUserId(userId); // Asignar el userId directamente
                    updatedSeller.setCreatedAt(LocalDateTime.now());
                    return sellerRepository.save(updatedSeller);
                });
    }


    // Método adicional para obtener todos los sellers (opcional)
    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    // Método adicional para eliminar un seller por userId (opcional)
    public void deleteSellerByUserId(Long userId) {
        sellerRepository.findByUserId(userId).ifPresent(sellerRepository::delete);
    }
    // Obtener por Seller ID (el ID único del Seller)
    public Optional<Seller> getSellerById(Long sellerId) {
        return sellerRepository.findById(sellerId);
    }
}