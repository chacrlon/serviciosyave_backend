package com.springboot.backend.andres.usersapp.usersbackend.repositories;

import com.springboot.backend.andres.usersapp.usersbackend.entities.VendorService;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;  

public interface VendorServiceRepository extends JpaRepository<VendorService, Long>{

	
}
