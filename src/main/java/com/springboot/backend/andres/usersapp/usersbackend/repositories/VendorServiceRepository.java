package com.springboot.backend.andres.usersapp.usersbackend.repositories;  

import com.springboot.backend.andres.usersapp.usersbackend.entities.VendorService;  
import java.util.List;  
import org.springframework.data.jpa.repository.JpaRepository;  
import org.springframework.data.jpa.repository.Query;  
import org.springframework.data.repository.query.Param;  

public interface VendorServiceRepository extends JpaRepository<VendorService, Long> {  
    // Cambiar el método para buscar por userId  
    List<VendorService> findByUserId(Long userId); 
    
    List<VendorService> findByCategoriaAndSubcategoria(String categoria, String subcategoria);  

    List<VendorService> findByPrecioBetween(Double minPrecio, Double maxPrecio);  

    List<VendorService> findByDestacado(Boolean destacado); 
    
    @Query(value = "SELECT vs FROM VendorService vs " +  
            "WHERE (6371 * acos(cos(radians(:clientLatitude)) * " +  
            "cos(radians(vs.latitude)) * " +  
            "cos(radians(vs.longitude) - radians(:clientLongitude)) + " +  
            "sin(radians(:clientLatitude)) * " +  
            "sin(radians(vs.latitude)))) < :distance")  
    List<VendorService> findNearbyServices(  
        @Param("clientLatitude") double clientLatitude,  
        @Param("clientLongitude") double clientLongitude,  
        @Param("distance") double distance);
    
    
}