package com.serviciosyave.repositories;  
  
import org.springframework.data.jpa.repository.JpaRepository;  
import com.serviciosyave.entities.Subcategory;  
import java.util.List;  

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {  
    // Método para encontrar subcategorías por ID de categoría  
    List<Subcategory> findByCategoryId(Long categoryId);  
}