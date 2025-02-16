package com.serviciosyave.controllers;

import com.serviciosyave.entities.PaymentToSeller;
import com.serviciosyave.repositories.PaymentToSellerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/admin/paymentseller") 
public class PaymentToSellerController {  

    @Autowired
    private PaymentToSellerRepository paymentToSellerRepository;

    @GetMapping
    public ResponseEntity<List<PaymentToSeller>> getAllTransactions() {
        return ResponseEntity.ok(paymentToSellerRepository.findAllWithDetails());
    }
    
}