package com.springboot.backend.andres.usersapp.usersbackend.controllers;  

import com.springboot.backend.andres.usersapp.usersbackend.entities.BankTransfer;  
import com.springboot.backend.andres.usersapp.usersbackend.services.BankTransferService;

import java.util.List;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;  
import org.springframework.validation.BindingResult;  
import org.springframework.web.bind.annotation.*;  

import jakarta.validation.Valid;  

@RestController  
@RequestMapping("/api/banktransfer")  
public class BankTransferController {  

    @Autowired  
    private BankTransferService bankTransferService;  

    @PostMapping("/create")  
    public ResponseEntity<?> createBankTransfer(@Valid @RequestBody BankTransfer bankTransfer, BindingResult result) {  
        if (result.hasErrors()) {  
            return ResponseEntity.badRequest().body(result.getAllErrors());  
        }  
        BankTransfer createdBankTransfer = bankTransferService.createBankTransfer(bankTransfer);  
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBankTransfer);  
    }  

    @GetMapping("/")  
    public List<BankTransfer> getAllBankTransfers() {  
        return bankTransferService.getAllBankTransfers();  
    }  
/*
    @GetMapping("/{id}")  
    public ResponseEntity<BankTransfer> getBankTransferById(@PathVariable Long id) {  
        Optional<BankTransfer> bankTransfer = bankTransferService.getBankTransferById(id);  
        return bankTransfer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());  
    }  
*/
    @PutMapping("/{id}")  
    public ResponseEntity<BankTransfer> updateBankTransfer(@PathVariable Long id, @Valid @RequestBody BankTransfer bankTransferDetails) {  
        BankTransfer updatedBankTransfer = bankTransferService.updateBankTransfer(id, bankTransferDetails);  
        return ResponseEntity.ok(updatedBankTransfer);  
    }  

    @DeleteMapping("/{id}")  
    public ResponseEntity<Void> deleteBankTransfer(@PathVariable Long id) {  
        bankTransferService.deleteBankTransfer(id);  
        return ResponseEntity.noContent().build();  
    }  
}