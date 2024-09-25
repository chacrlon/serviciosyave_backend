package com.springboot.backend.andres.usersapp.usersbackend.controllers;  

import com.springboot.backend.andres.usersapp.usersbackend.entities.MobilePayment;  
import com.springboot.backend.andres.usersapp.usersbackend.services.MobilePaymentService;

import java.util.List;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;  
import org.springframework.validation.BindingResult;  
import org.springframework.web.bind.annotation.*;  

import jakarta.validation.Valid;  

@RestController  
@RequestMapping("/api/mobilepayment")  
public class MobilePaymentController {  

    @Autowired  
    private MobilePaymentService mobilePaymentService;  

    @PostMapping("/create")  
    public ResponseEntity<?> createMobilePayment(@Valid @RequestBody MobilePayment mobilePayment, BindingResult result) {  
        if (result.hasErrors()) {  
            return ResponseEntity.badRequest().body(result.getAllErrors());  
        }  
        MobilePayment createdMobilePayment = mobilePaymentService.createMobilePayment(mobilePayment);  
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMobilePayment);  
    }  

    @GetMapping("/")  
    public List<MobilePayment> getAllMobilePayments() {  
        return mobilePaymentService.getAllMobilePayments();  
    }  
/*
    @GetMapping("/{id}")  
    public ResponseEntity<MobilePayment> getMobilePaymentById(@PathVariable Long id) {  
        Optional<MobilePayment> mobilePayment = mobilePaymentService.getMobilePaymentById(id);  
        return mobilePayment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());  
    }  
*/
    @PutMapping("/{id}")  
    public ResponseEntity<MobilePayment> updateMobilePayment(@PathVariable Long id, @Valid @RequestBody MobilePayment mobilePaymentDetails) {  
        MobilePayment updatedMobilePayment = mobilePaymentService.updateMobilePayment(id, mobilePaymentDetails);  
        return ResponseEntity.ok(updatedMobilePayment);  
    }  

    @DeleteMapping("/{id}")  
    public ResponseEntity<Void> deleteMobilePayment(@PathVariable Long id) {  
        mobilePaymentService.deleteMobilePayment(id);  
        return ResponseEntity.noContent().build();  
    }  
}