package com.springboot.backend.andres.usersapp.usersbackend.services;  

import org.springframework.stereotype.Service;  
import com.springboot.backend.andres.usersapp.usersbackend.entities.Category;
import com.springboot.backend.andres.usersapp.usersbackend.entities.Subcategory;
import com.springboot.backend.andres.usersapp.usersbackend.repositories.CategoryRepository;
import com.springboot.backend.andres.usersapp.usersbackend.repositories.SubcategoryRepository;
import com.springboot.backend.andres.usersapp.usersbackend.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired; 
import java.util.List;  

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
    
}