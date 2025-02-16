package com.serviciosyave.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;  
import com.serviciosyave.entities.Seller;
import com.serviciosyave.services.SellerService;
import jakarta.validation.Valid;  

@RestController  
@RequestMapping("/api/sellers")  
public class SellerController {  

    @Autowired  
    private SellerService sellerService;  

    @PostMapping("/register") 
    public ResponseEntity<?> register(@Valid @RequestBody Seller seller, BindingResult result) {  
       // Guardar usuario en la base de datos  
        Seller savedSeller = sellerService.saveSeller(seller);   
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSeller);  
    } 
    
    
    @PutMapping ("/{id}")
    	 public Seller updateSeller(@PathVariable Long id, @RequestBody Seller seller) {  
    	        seller.setId(id);  
    	        return sellerService.updateSeller(id, seller);
    	    }
    
}