package com.serviciosyave.repositories;
  
import org.springframework.data.jpa.repository.JpaRepository; 
import com.serviciosyave.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {  
    
}