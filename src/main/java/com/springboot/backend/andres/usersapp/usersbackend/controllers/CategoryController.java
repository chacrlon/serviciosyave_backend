package com.springboot.backend.andres.usersapp.usersbackend.controllers;  

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;  
import org.springframework.web.bind.annotation.*;

import com.springboot.backend.andres.usersapp.usersbackend.entities.Binance;
import com.springboot.backend.andres.usersapp.usersbackend.entities.Category;
import com.springboot.backend.andres.usersapp.usersbackend.entities.Subcategory;
import com.springboot.backend.andres.usersapp.usersbackend.services.CategoryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;  
import java.util.Map;
import java.util.Optional;  

@RestController  
@RequestMapping("/api/categories") 
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {  

    @Autowired  
    private CategoryService categoryService;  

    @GetMapping  
    public List<Category> listCategories() {  
        return categoryService.listCategories();  
    }  

    @PostMapping  
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {  
        Category createdCategory = categoryService.createCategory(category);  
        // Asegúrate de que las subcategorías se devuelvan, aunque sea null  
        if (createdCategory.getSubcategories() == null) {  
            createdCategory.setSubcategories(new ArrayList<>());  
        }  
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);  
    }  

    @PutMapping("/{id}")  
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {  
        Map<String, Object> response = new HashMap<>();  
        Category categoryActual = categoryService.getCateoryById(id).orElse(null);  
        
        if (categoryActual == null) {  
            response.put("mensaje", "Error: no se pudo editar, la categoría ID: " + id + " no existe en la base de datos!");  
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);  
        }  

        try {  
            categoryActual.setName(categoryDetails.getName());  
            // O cualquier otro campo que necesites actualizar  
            categoryService.updateCategory(id, categoryActual);  
            
            // Asegúrate de que las subcategorías se devuelvan  
            if (categoryActual.getSubcategories() == null) {  
                categoryActual.setSubcategories(new ArrayList<>());  
            }  
        } catch (Exception e) {  
            response.put("mensaje", "Error al actualizar la categoría en la base de datos");  
            response.put("error", e.getMessage().concat(": ").concat(e.getCause() != null ? e.getCause().getMessage() : ""));  
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
        }  

        response.put("mensaje", "La categoría ha sido actualizada con éxito!");  
        response.put("categoría", categoryActual);  

        return new ResponseEntity<>(response, HttpStatus.OK);  
    }  

    @DeleteMapping("/{id}")  
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {  
        categoryService.deleteCategory(id);  
        return ResponseEntity.noContent().build();  
    }  
    
    @GetMapping("/{id}/subcategories")  
    public ResponseEntity<List<Subcategory>> getSubcategoriesByCategoryId(@PathVariable Long id) {  
        List<Subcategory> subcategories = categoryService.getSubcategoriesByCategoryId(id);  
        if (subcategories.isEmpty()) {  
            return ResponseEntity.noContent().build(); // Devuelve 204 si no hay subcategorías  
        }  
        return ResponseEntity.ok(subcategories); // Devuelve 200 con la lista de subcategorías  
    }
}  
