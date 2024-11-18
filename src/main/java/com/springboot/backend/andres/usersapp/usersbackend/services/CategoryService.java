package com.springboot.backend.andres.usersapp.usersbackend.services;  

import org.springframework.stereotype.Service;

import com.springboot.backend.andres.usersapp.usersbackend.entities.Binance;
import com.springboot.backend.andres.usersapp.usersbackend.entities.Category;
import com.springboot.backend.andres.usersapp.usersbackend.entities.Subcategory;
import com.springboot.backend.andres.usersapp.usersbackend.repositories.CategoryRepository;
import com.springboot.backend.andres.usersapp.usersbackend.repositories.SubcategoryRepository;
import com.springboot.backend.andres.usersapp.usersbackend.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired; 
import java.util.List;
import java.util.Optional;  


@Service  
public class CategoryService {  

	@Autowired  
    private CategoryRepository categoryRepository; 
	// Suponiendo que tienes un repositorio para las subcategorías  
    @Autowired  
    private SubcategoryRepository subcategoryRepository;  

    public List<Category> listCategories() {  
        return categoryRepository.findAll();  
    }  

    public Category createCategory(Category category) {  
        return categoryRepository.save(category);  
    }  
    
    public Optional<Category> getCateoryById(Long id) {  
        return categoryRepository.findById(id);  
    } 

    public Category updateCategory(Long id, Category category) {  
    	category.setId(id);  
        return categoryRepository.save(category);  
    }  

    public void deleteCategory(Long id) {  
        Category category = categoryRepository.findById(id)  
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));  
        categoryRepository.delete(category);  
    } 
    
 // Método para obtener subcategorías por ID de categoría  
    public List<Subcategory> getSubcategoriesByCategoryId(Long categoryId) {  
        return subcategoryRepository.findByCategoryId(categoryId);  
    }  
    
}