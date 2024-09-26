package com.springboot.backend.andres.usersapp.usersbackend.controllers;  

import com.springboot.backend.andres.usersapp.usersbackend.entities.User;  
import com.springboot.backend.andres.usersapp.usersbackend.entities.VendorService;  
import com.springboot.backend.andres.usersapp.usersbackend.services.JpaUserDetailsService;  
import com.springboot.backend.andres.usersapp.usersbackend.services.VendorServiceService;  
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
public class ServiceController {  

    @Autowired  
    private VendorServiceService vendorServiceService;   
    
    @Autowired  
    private JpaUserDetailsService userDetailsService;  

    @PostMapping("/create")  
    public ResponseEntity<VendorService> createService(@RequestBody VendorService vendorService) {  
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  
        Long userId = (Long) authentication.getDetails();  

        User user = userDetailsService.findById(userId);  

        if (user == null) {  
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  
        }  

        vendorService.setUsers(user);  
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
        // Obtener la autenticación actual  
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  
        Long userId = (Long) authentication.getDetails(); // Obtener el userId desde los detalles  

        // Obtener todos los servicios creados por el usuario autenticado  
        List<VendorService> services = vendorServiceService.getServicesByUserId(userId);  
        
        return ResponseEntity.ok(services);  
    }

    @PutMapping("/{id}")  
    public ResponseEntity<VendorService> updateService(@PathVariable Long id, @RequestBody VendorService vendorServiceDetails) {  
        VendorService updatedService = vendorServiceService.updateService(id, vendorServiceDetails);  
        return ResponseEntity.ok(updatedService);  
    }  

    @DeleteMapping("/{id}")  
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {  
        vendorServiceService.deleteService(id);  
        return ResponseEntity.noContent().build();  
    }  
}