package com.serviciosyave.controllers;

import com.serviciosyave.entities.Category;
import com.serviciosyave.entities.ServiceFilter;
import com.serviciosyave.entities.Subcategory;
import com.serviciosyave.entities.User;
import com.serviciosyave.entities.VendorService;
import com.serviciosyave.exceptions.ResourceNotFoundException;
import com.serviciosyave.services.CategoryService;
import com.serviciosyave.services.JpaUserDetailsService;
import com.serviciosyave.services.SubcategoryService;
import com.serviciosyave.services.VendorServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController  
@RequestMapping("/api/service") 
@CrossOrigin(origins = "http://localhost:4200") 
public class ServiceController {  
	@Autowired  
    private VendorServiceService vendorServiceService;   
    
    @Autowired  
    private JpaUserDetailsService userDetailsService;   
    
    @Autowired  
    private CategoryService categoryService;  

    @Autowired  
    private SubcategoryService subcategoryService;  
    
    @GetMapping("/available")  
    public ResponseEntity<List<VendorService>> getAvailableServices(@RequestParam Double lat, @RequestParam Double lon) {
        List<VendorService> services = vendorServiceService.getAllAvailableServices(lat, lon);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/available/{id}")  
    public ResponseEntity<List<VendorService>> getAvailableServicesByProfessional(@PathVariable Long id) {
        List<VendorService> services = vendorServiceService.getServicesByUserId(id);
        return ResponseEntity.ok(services);
    } 

    @PostMapping("/filter")  
    public ResponseEntity<List<VendorService>> filterServices(@RequestBody ServiceFilter filter) {  
        List<VendorService> filteredServices = vendorServiceService.filterServices(filter);  
        return ResponseEntity.ok(filteredServices);  
    }  

    @PostMapping("/create")  
    public ResponseEntity<VendorService> createService(@RequestBody VendorService vendorService) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  
        Long userId = (Long) authentication.getDetails();  

        User user = userDetailsService.findById(userId);  

        if (user == null) {  
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  
        }  

        // Establecer la categoría y subcategoría  
        if (vendorService.getCategory() != null) {  
            Category category = categoryService.getCateoryById(vendorService.getCategory().getId())  
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));  
            vendorService.setCategory(category);  
        }  

        if (vendorService.getSubcategory() != null) {  
            Subcategory subcategory = subcategoryService.getSubcategoryById(vendorService.getSubcategory().getId())  
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory not found"));  
            vendorService.setSubcategory(subcategory);  
        }  

        vendorService.setUserId(userId); // Establecer el ID del usuario  
        VendorService createdService = vendorServiceService.createService(vendorService);  
        return ResponseEntity.status(HttpStatus.CREATED).body(createdService);   
    }  

    @GetMapping("/{id}")  
    public ResponseEntity<VendorService> getServiceById(@PathVariable Long id) {  
        Optional<VendorService> service = vendorServiceService.getServiceById(id);  
        return service.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());  
    }  

    @GetMapping("/")   
    public ResponseEntity<List<VendorService>> getAllServices() {  
        return ResponseEntity.ok(vendorServiceService.getAllServices());  
    }  

    @GetMapping("/user")  
    public ResponseEntity<List<VendorService>> getAllServicesByUser() {  
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  
        Long userId = (Long) authentication.getDetails();  

        List<VendorService> services = vendorServiceService.getServicesByUserId(userId);  
        return ResponseEntity.ok(services);  
    }  

    @PutMapping("/{id}")  
    public ResponseEntity<VendorService> updateService(@PathVariable Long id, @RequestBody VendorService vendorServiceDetails) {  
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  
        Long userId = (Long) authentication.getDetails();  

        VendorService existingService = vendorServiceService.getServiceById(id)  
            .orElseThrow();  

        if (!existingService.getUserId().equals(userId)) {  
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();  
        }  

        VendorService updatedService = vendorServiceService.updateService(id, vendorServiceDetails);  
        return ResponseEntity.ok(updatedService);  
    }  

    @DeleteMapping("/{id}")  
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {  
        vendorServiceService.deleteService(id);  
        return ResponseEntity.noContent().build();  
    }
    
    
    @GetMapping("/nearby")  
    public ResponseEntity<List<VendorService>> getNearbyServices(  
            @RequestParam double latitude,  
            @RequestParam double longitude,  
            @RequestParam double distance) {  
        
        List<VendorService> nearbyServices = vendorServiceService.getNearbyServices(latitude, longitude, distance);  
        return ResponseEntity.ok(nearbyServices);  
    }  
}