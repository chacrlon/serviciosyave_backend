package com.springboot.backend.andres.usersapp.usersbackend.controllers;  

import com.springboot.backend.andres.usersapp.usersbackend.entities.VendorService;
import com.springboot.backend.andres.usersapp.usersbackend.services.JpaUserDetailsService;
import com.springboot.backend.andres.usersapp.usersbackend.services.VendorServiceService;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;  
import java.util.List;  
import java.util.Optional;  
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController  
@RequestMapping("/api/service")  
public class ServiceController {  

    @Autowired  
    private VendorServiceService vendorServiceService;   
    
    @Autowired
    private JpaUserDetailsService userDetailsService;
    

    @RequestMapping(value = "/current-user", method = RequestMethod.GET)  
    public ResponseEntity<UserDetails> getCurrentUser() {
     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
     UserDetails currentUser = userDetailsService.loadUserByUsername(authentication.getName());
     return ResponseEntity.ok(currentUser);
    }
    
    @PostMapping("/vendorservices")  
    public void createVendorService(@RequestBody VendorService vendorService) {  
        // Aquí puedes obtener el ID del usuario autenticado usando el servicio JpaUserDetailsService  
        Long userId = userDetailsService.getUserId(); // Obtener el ID del usuario autenticado  

        // Imprimir el ID del usuario  
        if (userId != null) {  
            System.out.println("El ID de usuario autenticado es: " + userId);  
        } else {  
            System.out.println("No hay un usuario autenticado.");  
        }  

        // Resto del código para crear el servicio de vendedor  
    }

    @PostMapping("/create")  
    public ResponseEntity<VendorService> createService(@RequestBody VendorService vendorService) {  
    	  // Aquí puedes obtener el ID del usuario autenticado usando el servicio JpaUserDetailsService  
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  
        
        Long userId = (Long) authentication.getDetails(); // Obtener el userId desde los detalles  
        
        // Imprimir el ID del usuario  

            System.out.println("El ID de usuario autenticado es: " + userId);  

        // Aquí podrías también llenar el objeto vendorService con el userId, si lo necesitas
        // vendorService.setUserId(userId);
        
        // Llama al servicio para crear el VendorService
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
