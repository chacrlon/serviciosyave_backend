package com.springboot.backend.andres.usersapp.usersbackend.repositories;  
  
import org.springframework.data.jpa.repository.JpaRepository;  
import com.springboot.backend.andres.usersapp.usersbackend.entities.Subcategory;  
import java.util.List;  

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {  
    // Método para encontrar subcategorías por ID de categoría  
    List<Subcategory> findByCategoryId(Long categoryId);  
}