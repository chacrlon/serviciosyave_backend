package com.springboot.backend.andres.usersapp.usersbackend.repositories;  

import com.springboot.backend.andres.usersapp.usersbackend.entities.VendorService;  
import java.util.List;  
import org.springframework.data.jpa.repository.JpaRepository;  
import org.springframework.data.jpa.repository.Query;  
import org.springframework.data.repository.query.Param;  

public interface VendorServiceRepository extends JpaRepository<VendorService, Long> {  
    // Cambiar el método para buscar por userId  
    List<VendorService> findByUserId(Long userId);  
    
    @Query(value = "SELECT vs FROM VendorService vs " +  
            "JOIN vs.ubicacion u " +  
            "WHERE (6371 * acos(cos(radians(:clientLatitude)) * " +  
            "cos(radians(u.latitude)) * " +  
            "cos(radians(u.longitude) - radians(:clientLongitude)) + " +  
            "sin(radians(:clientLatitude)) * " +  
            "sin(radians(u.latitude)))) < :distance", nativeQuery = true)  
    List<VendorService> findNearbyServices(  
        @Param("clientLatitude") double clientLatitude,  
        @Param("clientLongitude") double clientLongitude,  
        @Param("distance") double distance);  
}