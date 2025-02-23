package com.serviciosyave.services;

import com.serviciosyave.entities.Seller;
import com.serviciosyave.repositories.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;

    @Autowired
    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    // Crear un nuevo perfil de vendedor
    public Seller createSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    // Obtener todos los perfiles de vendedores
    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    // Obtener un perfil de vendedor por ID
    public Optional<Seller> getSellerById(Long id) {
        return sellerRepository.findById(id);
    }

    // Actualizar un perfil de vendedor existente
    public Seller updateSeller(Long id, Seller updatedSeller) {
        return sellerRepository.findById(id)
                .map(seller -> {
                    seller.setFullName(updatedSeller.getFullName());
                    seller.setIdNumber(updatedSeller.getIdNumber());
                    seller.setGender(updatedSeller.getGender());
                    seller.setProfession(updatedSeller.getProfession());
                    seller.setYearsOfExperience(updatedSeller.getYearsOfExperience());
                    seller.setSkillsDescription(updatedSeller.getSkillsDescription());
                    seller.setProfilePicture(updatedSeller.getProfilePicture());
                    seller.setGalleryImages(updatedSeller.getGalleryImages());
                    seller.setEmail(updatedSeller.getEmail());
                    seller.setServiceName(updatedSeller.getServiceName());
                    seller.setCertifications(updatedSeller.getCertifications());
                    seller.setExtras(updatedSeller.getExtras());
                    return sellerRepository.save(seller);
                })
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado con ID: " + id));
    }

    // Eliminar un perfil de vendedor por ID
    public void deleteSeller(Long id) {
        sellerRepository.deleteById(id);
    }
}