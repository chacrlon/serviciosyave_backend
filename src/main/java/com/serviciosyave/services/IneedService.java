package com.serviciosyave.services;  

import com.serviciosyave.controllers.NotificationController;
import com.serviciosyave.entities.Category;
import com.serviciosyave.entities.Subcategory;
import com.serviciosyave.entities.User;
import com.serviciosyave.repositories.CategoryRepository;
import com.serviciosyave.repositories.SubcategoryRepository;
import com.serviciosyave.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.serviciosyave.entities.Ineed;
import com.serviciosyave.repositories.IneedRepository;
import java.util.List;

@Service  
public class IneedService {  

    @Autowired
    private final IneedRepository ineedRepository;

    @Autowired
    private CategoryRepository categoryRepository; // Nuevo repositorio añadido

    @Autowired
    private SubcategoryRepository subcategoryRepository; // Opcional si usas subcategorías

    @Autowired
    private GPSService gpsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationController notificationController;

    public Ineed findById(Long id) {
        return ineedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ineed no encontrado con ID: " + id));
    }

    public Ineed getIneedById(Long id) {
        return ineedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ineed no encontrado con ID: " + id));
    }

    public void acceptIneed(Long ineedId, Long providerId) {
        Ineed ineed = ineedRepository.findById(ineedId)
                .orElseThrow(() -> new RuntimeException("Ineed no encontrado"));

        // Obtener usuarios involucrados
        User buyer = userRepository.findById(ineed.getUserId())
                .orElseThrow(() -> new RuntimeException("Comprador no encontrado"));
        User provider = userRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        // Crear mensaje de notificación
        String notificationMessage = "Tu requerimiento '" + ineed.getTitulo() + "' ha sido aceptado por " + provider.getUsername();

        Double ineedAmount = ineed.getPresupuesto();
        // Enviar email
        emailService.sendEmail(
                buyer.getEmail(),
                "Requerimiento Aceptado",
                notificationMessage
        );

        // Crear notificación (Buyer es el userType)
        notificationController.notifyUser(
                buyer.getId(),      // userId (destinatario)
                provider.getId(),   // userId2 (proveedor)
                notificationMessage,
                "Buyer",           // userType
                null,              // vendorServiceId (no aplica)
                ineedId,
                "requerimiento",
                "no_pagado",
                ineedAmount,
                null
        );
    }

    public IneedService(IneedRepository ineedRepository) {
        this.ineedRepository = ineedRepository;
    }

    public Ineed crearNecesidad(Ineed ineed) throws Exception {
        // Validar que la categoría tenga formulario
        Category categoriaCompleta = categoryRepository.findById(ineed.getCategory().getId())
                .orElseThrow(() -> new ValidationException("Categoría no encontrada"));

        // Validar formulario
        if(categoriaCompleta.getFormulario() == null) {
            throw new ValidationException("La categoría no tiene formulario configurado");
        }

        // Asignar la categoría completa al Ineed
        ineed.setCategory(categoriaCompleta);

        // Opcional: Hacer lo mismo para subcategoría si es necesario
        if(ineed.getSubcategory() != null) {
            Subcategory subcategoriaCompleta = subcategoryRepository.findById(ineed.getSubcategory().getId())
                    .orElseThrow(() -> new ValidationException("Subcategoría no encontrada"));
            ineed.setSubcategory(subcategoriaCompleta);
        }

        String address = gpsService.fetchAddressFromCoordinates(ineed.getLatitude(), ineed.getLongitude());
        ineed.setAddress(address);
        return ineedRepository.save(ineed);
    }

    public List<Ineed> obtenerNecesidades(Double customerLat, Double customerLong) {
        List<Ineed> necesidades = ineedRepository.findAll();

        for (Ineed necesidad : necesidades) {
            try {
                double latitudIneed = necesidad.getLatitude();
                double longitudIneed = necesidad.getLongitude();
                double nearby = gpsService.nearbyDistance(latitudIneed, longitudIneed, customerLat, customerLong);
                necesidad.setNearby(nearby);
            } catch (Exception e) {
                System.err.println("Error processing necesidad with ID " + necesidad.getId() + ": " + e.getMessage());
            }
        }
        return necesidades;
    }

    private String[] parseCoordinates(String ubicacion) {
        String[] coordenadas = ubicacion.split(", ");
        return new String[] {
            coordenadas[0].split(": ")[1],
            coordenadas[1].split(": ")[1]
        };
    }

    public Ineed obtenerNecesidadPorId(Long id) {  
        return ineedRepository.findById(id).orElse(null);  
    }  

    public void eliminarNecesidad(Long id) {  
        ineedRepository.deleteById(id);  
    } 
    
}