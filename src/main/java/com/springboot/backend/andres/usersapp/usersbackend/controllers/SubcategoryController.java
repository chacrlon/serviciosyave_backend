package com.springboot.backend.andres.usersapp.usersbackend.controllers;  

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;  
import org.springframework.web.bind.annotation.*;

import com.springboot.backend.andres.usersapp.usersbackend.entities.Category;
import com.springboot.backend.andres.usersapp.usersbackend.entities.Subcategory;
import com.springboot.backend.andres.usersapp.usersbackend.services.CategoryService;
import com.springboot.backend.andres.usersapp.usersbackend.services.SubcategoryService;

import java.util.ArrayList;
import java.util.HashMap;  
import java.util.Map;  
import java.util.List;  

@RestController  
@RequestMapping("/api/subcategories")  
@CrossOrigin(origins = "http://localhost:4200")
public class SubcategoryController {  

	@Autowired  
    private CategoryService categoryService;  

	
    @Autowired  
    private SubcategoryService subcategoryService;  

    @GetMapping  
    public List<Subcategory> listSubcategories() {  
        return subcategoryService.listSubcategories();  
    }  

    @PostMapping("/category/{categoryId}")  
    public ResponseEntity<Category> createSubcategory(@PathVariable Long categoryId, @RequestBody Subcategory subcategory) {  
        Subcategory createdSubcategory = subcategoryService.createSubcategory(categoryId, subcategory);  
        // Obtener la categoría completa con las subcategorías después de agregar  
        Category category = categoryService.getCateoryById(categoryId)  
                .orElseThrow();  
        
        // Asegúrate de que las subcategorías se devuelvan  
        if (category.getSubcategories() == null) {  
            category.setSubcategories(new ArrayList<>());  
        }  
        category.getSubcategories().add(createdSubcategory); // Agregar la subcategoría creada a la lista  
        return new ResponseEntity<>(category, HttpStatus.CREATED);  
    } 

    @PutMapping("/{id}")  
    public ResponseEntity<?> updateSubcategory(@PathVariable Long id, @RequestBody Subcategory subcategoryDetails) {  
        Map<String, Object> response = new HashMap<>();  
        Subcategory subcategoryActual = subcategoryService.listSubcategories().stream()  
                .filter(sub -> sub.getId().equals(id))  
                .findFirst()  
                .orElse(null);  

        if (subcategoryActual == null) {  
            response.put("mensaje", "Error: no se pudo editar, la subcategoría ID: " + id + " no existe en la base de datos!");  
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);  
        }  

        try {  
            subcategoryActual.setName(subcategoryDetails.getName());  
            subcategoryService.updateSubcategory(id, subcategoryActual);  

            // Obtener la categoría correspondiente de la subcategoría actualizada  
            Category category = categoryService.getCateoryById(subcategoryActual.getCategory().getId()).orElse(null);  
            
            // Asegúrate de que las subcategorías se devuelvan  
            if (category != null && category.getSubcategories() == null) {  
                category.setSubcategories(new ArrayList<>());  
            }  
            
            // Devuelve la categoría completa, incluida la subcategoría actualizada  
            return new ResponseEntity<>(category, HttpStatus.OK);  

        } catch (Exception e) {  
            response.put("mensaje", "Error al actualizar la subcategoría en la base de datos");  
            response.put("error", e.getMessage().concat(": ").concat(e.getCause() != null ? e.getCause().getMessage() : ""));  
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
        }  
    }

    @DeleteMapping("/{id}")  
    public ResponseEntity<Void> deleteSubcategory(@PathVariable Long id) {  
        subcategoryService.deleteSubcategory(id);  
        return ResponseEntity.noContent().build();  
    }
}  