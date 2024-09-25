package com.springboot.backend.andres.usersapp.usersbackend.controllers;  

import com.springboot.backend.andres.usersapp.usersbackend.entities.Binance;
import com.springboot.backend.andres.usersapp.usersbackend.entities.User;
import com.springboot.backend.andres.usersapp.usersbackend.services.BinanceService;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;  
import org.springframework.validation.BindingResult;  
import org.springframework.web.bind.annotation.*;  

import jakarta.validation.Valid;  
import java.util.List;  

@RestController  
@RequestMapping("/api/binance")  
public class BinanceController {  

    @Autowired  
    private BinanceService binanceService;  

    @PostMapping("/create")  
    public ResponseEntity<?> createBinance(@Valid @RequestBody Binance binance, BindingResult result) {  
        if (result.hasErrors()) {  
            return ResponseEntity.badRequest().body(result.getAllErrors());  
        }  

        Binance createdBinance = binanceService.createBinance(binance);  
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBinance);  
    }  
    
    @GetMapping("/")  
    public ResponseEntity<List<Binance>> getAllBinances() {  
        List<Binance> binances = binanceService.getAllBinances();  
        return ResponseEntity.ok(binances);  
    }  
/*
    @GetMapping("/{id}")  
    public ResponseEntity<?> getBinanceById(@PathVariable Long id) {  
        return binanceService.getBinanceById(id)  
            .map(binance -> ResponseEntity.ok(binance))  
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Binance not found"));  
    }  
*/
    @PutMapping("/update/{id}")  
    public ResponseEntity<?> updateBinance(@PathVariable Long id, @Valid @RequestBody Binance binance, BindingResult result) {  
        if (result.hasErrors()) {  
            return ResponseEntity.badRequest().body(result.getAllErrors());  
        }  

        Binance updatedBinance = binanceService.updateBinance(id, binance);  
        return ResponseEntity.ok(updatedBinance);  
    }  

    @DeleteMapping("/delete/{id}")  
    public ResponseEntity<?> deleteBinance(@PathVariable Long id) {  
        binanceService.deleteBinance(id);  
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  
    }  
}