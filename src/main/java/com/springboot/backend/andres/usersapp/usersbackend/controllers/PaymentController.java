package com.springboot.backend.andres.usersapp.usersbackend.controllers;  

import com.springboot.backend.andres.usersapp.usersbackend.entities.Payment;
import com.springboot.backend.andres.usersapp.usersbackend.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;  
import org.springframework.security.core.Authentication;  
import org.springframework.security.core.context.SecurityContextHolder;  
import org.springframework.web.bind.annotation.*;  

@RestController  
@RequestMapping("/api/payment")  
public class PaymentController {  

    @Autowired  
    private PaymentService paymentService;   

    @PostMapping("/create")  
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {  
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  
        Long userId = (Long) authentication.getDetails();   

        payment.setUserId(userId);  
        Payment createdPayment = paymentService.createdPayment(payment);  
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPayment);  
    }  

    
}