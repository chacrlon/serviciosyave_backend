package com.serviciosyave.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.ResponseEntity;  
import org.springframework.web.bind.annotation.*;  
import org.springframework.web.multipart.MultipartFile;

import com.serviciosyave.entities.Seller;
import com.serviciosyave.services.SellerService;  

@RestController  
@RequestMapping("/api/sellers")  
public class SellerController {  

    @Autowired  
    private SellerService sellerService;  

    @PostMapping("/register") // Tengo que ver en  cual ruta se puede almacenar. 
    public ResponseEntity<Seller> registerSeller(@RequestParam String name,  
                                                  @RequestParam String email,  
                                                  @RequestParam int yearsOfExperience,  
                                                  @RequestParam String serviceDescription,  
                                                  @RequestParam MultipartFile profilePicture) {  
        Seller seller = new Seller();  
        seller.setName(name);  
        seller.setEmail(email);  
        seller.setYearsOfExperience(yearsOfExperience);  
        seller.setServiceDescription(serviceDescription);  

        try {  
            Seller savedSeller = sellerService.registerSeller(seller, profilePicture);  
            return ResponseEntity.ok(savedSeller);  
        } catch (IOException e) {  
            return ResponseEntity.status(500).body(null);  
        }  
    }  
}