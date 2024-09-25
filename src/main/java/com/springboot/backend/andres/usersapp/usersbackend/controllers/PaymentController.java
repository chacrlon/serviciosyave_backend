package com.springboot.backend.andres.usersapp.usersbackend.controllers;  

import com.springboot.backend.andres.usersapp.usersbackend.entities.PaymentTransaction;  
import com.springboot.backend.andres.usersapp.usersbackend.services.PaymentService;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;  
import org.springframework.validation.BindingResult;  
import org.springframework.web.bind.annotation.*;  

import jakarta.validation.Valid;  

import java.util.HashMap;  
import java.util.Map;  

@RestController  
@RequestMapping("/api/payments")  
public class PaymentController {  

    @Autowired  
    private PaymentService paymentService;  

    @PostMapping("/transaction")  
    public ResponseEntity<?> createTransaction(@Valid @RequestBody PaymentTransaction transaction, BindingResult result) {  
        if (result.hasErrors()) {  
            return validation(result);  
        }  

        // Lógica para crear la transacción  
        PaymentTransaction savedTransaction = paymentService.createPaymentTransaction(  
                transaction.getMonto(),  
                transaction.getReferencia(),  
                transaction.getVendorService(),  
                transaction.getUser(),  
                transaction.getPaymentMethod()  
        );  

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);  
    }  

    // Método para validar errores  
    private ResponseEntity<?> validation(BindingResult result) {  
        Map<String, String> errors = new HashMap<>();  
        result.getFieldErrors().forEach(error -> {  
            errors.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());  
        });  
        return ResponseEntity.badRequest().body(errors);  
    }  
}

