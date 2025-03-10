package com.serviciosyave.controllers;

import com.serviciosyave.entities.Seller;
import com.serviciosyave.services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    private final SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PostMapping("/register")
    public ResponseEntity<Seller> createOrUpdateSeller(@RequestBody Seller seller) {
        // Establecer la fecha de creación si no está presente
        if (seller.getCreatedAt() == null) {
            seller.setCreatedAt(LocalDateTime.now());
        }

        // Convertir Base64 a bytes (si es necesario)
        if (seller.getDniFrontName() != null) {
            seller.setDniFrontName(Base64.getEncoder().encodeToString(seller.getDniFrontName().getBytes()));
        }
        if (seller.getDniBackName() != null) {
            seller.setDniBackName(Base64.getEncoder().encodeToString(seller.getDniBackName().getBytes()));
        }
        if (seller.getSelfieName() != null) {
            seller.setSelfieName(Base64.getEncoder().encodeToString(seller.getSelfieName().getBytes()));
        }
        if (seller.getUniversityTitleName() != null) {
            seller.setUniversityTitleName(Base64.getEncoder().encodeToString(seller.getUniversityTitleName().getBytes()));
        }
        if (seller.getCertificationsNames() != null) {
            seller.setCertificationsNames(seller.getCertificationsNames().stream()
                    .map(cert -> Base64.getEncoder().encodeToString(cert.getBytes()))
                    .collect(Collectors.toList()));
        }
        if (seller.getGalleryImagesNames() != null) {
            seller.setGalleryImagesNames(seller.getGalleryImagesNames().stream()
                    .map(img -> Base64.getEncoder().encodeToString(img.getBytes()))
                    .collect(Collectors.toList()));
        }

        // Guardar o actualizar el seller
        Seller savedSeller = sellerService.updateSellerByUserId(seller.getUserId(), seller);
        return ResponseEntity.ok(savedSeller);
    }

    @GetMapping("/seller/{userId}")
    public ResponseEntity<Seller> getSellerByUserId(@PathVariable Long userId) {
        return sellerService.getSellerByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}