package com.springboot.backend.andres.usersapp.usersbackend.repositories;  
  
import org.springframework.data.jpa.repository.JpaRepository;  
import com.springboot.backend.andres.usersapp.usersbackend.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {  
    
}