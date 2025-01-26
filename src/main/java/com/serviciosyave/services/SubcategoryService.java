package com.serviciosyave.services;

import org.springframework.stereotype.Service;
import com.serviciosyave.entities.Category;
import com.serviciosyave.entities.Subcategory;
import com.serviciosyave.repositories.CategoryRepository;
import com.serviciosyave.repositories.SubcategoryRepository;
import com.serviciosyave.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service  
public class SubcategoryService {  

	@Autowired  
    private SubcategoryRepository subcategoryRepository;  

    @Autowired  
    private CategoryRepository categoryRepository;  

    public List<Subcategory> listSubcategories() {  
        return subcategoryRepository.findAll();  
    }  

    public Subcategory createSubcategory(Long categoryId, Subcategory subcategory) {  
        Category category = categoryRepository.findById(categoryId)  
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));  

        subcategory.setCategory(category);  
        return subcategoryRepository.save(subcategory);  
    }  

    public Subcategory updateSubcategory(Long id, Subcategory subcategoryDetails) {  
        Subcategory subcategory = subcategoryRepository.findById(id)  
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory not found"));  
        
        // Actualiza solo si hay datos presentes en subcategoryDetails  
        if (subcategoryDetails.getName() != null) {  
            subcategory.setName(subcategoryDetails.getName());  
        }  
        
        // Puedes agregar más propiedades a actualizar aquí si es necesario  
        
        return subcategoryRepository.save(subcategory);  
    } 

    public void deleteSubcategory(Long id) {  
        Subcategory subcategory = subcategoryRepository.findById(id)  
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory not found"));  
        subcategoryRepository.delete(subcategory);  
    }  
    
 // Método para obtener subcategorías por ID de categoría  
    public List<Subcategory> getSubcategoriesByCategoryId(Long categoryId) {  
        return subcategoryRepository.findByCategoryId(categoryId);  
    }
 // Método para obtener una subcategoría por ID  
    public Optional<Subcategory> getSubcategoryById(Long id) {  
        return subcategoryRepository.findById(id);  
    }  
}