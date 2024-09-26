// En VendorServiceRepository.java  
package com.springboot.backend.andres.usersapp.usersbackend.repositories;  

import com.springboot.backend.andres.usersapp.usersbackend.entities.VendorService;  
import java.util.List;  
import org.springframework.data.jpa.repository.JpaRepository;  

public interface VendorServiceRepository extends JpaRepository<VendorService, Long> {  
    // Cambiar el método para encontrar servicios por el ID del usuario asociado  
    List<VendorService> findByUsers_Id(Long userId);  
}



/*
@Query("SELECT vs FROM VendorService vs where vs.users.id = ?1")
List<VendorService> findByUsers_Id(Long userId); */