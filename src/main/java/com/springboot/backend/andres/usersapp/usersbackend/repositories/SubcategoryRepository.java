package com.springboot.backend.andres.usersapp.usersbackend.repositories;  
  
import org.springframework.data.jpa.repository.JpaRepository;  
import com.springboot.backend.andres.usersapp.usersbackend.entities.Subcategory;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {  
    
}