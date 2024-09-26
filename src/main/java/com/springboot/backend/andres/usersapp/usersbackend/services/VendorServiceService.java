package com.springboot.backend.andres.usersapp.usersbackend.services;  

import java.util.List;  
import java.util.Optional;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Service;  
import com.springboot.backend.andres.usersapp.usersbackend.entities.VendorService;  
import com.springboot.backend.andres.usersapp.usersbackend.repositories.VendorServiceRepository;  

@Service  
public class VendorServiceService {  

    @Autowired  
    private VendorServiceRepository vendorServiceRepository;  

    public VendorService createService(VendorService service) {  
        return vendorServiceRepository.save(service);  
    }  

    public List<VendorService> getAllServices() {  
        return vendorServiceRepository.findAll();  
    }  

    public Optional<VendorService> getServiceById(Long id) {  
        return vendorServiceRepository.findById(id);  
    }  

    public VendorService updateService(Long id, VendorService serviceDetails) {  
        VendorService serviceToUpdate = vendorServiceRepository.findById(id).orElseThrow();  
        // Actualiza los campos modificables  
        serviceToUpdate.setNombre(serviceDetails.getNombre());  
        serviceToUpdate.setDescripcion(serviceDetails.getDescripcion());  
        serviceToUpdate.setPrecio(serviceDetails.getPrecio());  
        serviceToUpdate.setDestacado(serviceDetails.getDestacado());  
        return vendorServiceRepository.save(serviceToUpdate);  
    }  

    public void deleteService(Long id) {  
        VendorService serviceToDelete = vendorServiceRepository.findById(id).orElseThrow();  
        vendorServiceRepository.delete(serviceToDelete);  
    } 
    
 // Método para obtener todos los servicios por el ID del usuario  
    public List<VendorService> getServicesByUserId(Long userId) {  
        return vendorServiceRepository.findByUsers_Id(userId); // Asegúrate de definir este método en tu repositorio  
    }  
}