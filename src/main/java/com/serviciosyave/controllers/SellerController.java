package com.serviciosyave.controllers;

import com.serviciosyave.entities.Seller;
import com.serviciosyave.services.LocationService;
import com.serviciosyave.services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final LocationService locationService;

    @Autowired
    public SellerController(SellerService sellerService, LocationService locationService) {
        this.sellerService = sellerService;
        this.locationService = locationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createOrUpdateSeller(@RequestBody Seller seller){
        try {
            // Validar coordenadas
            if (seller.getLatitude() == 0 || seller.getLongitude() == 0) {
                return ResponseEntity.badRequest().body("Coordenadas obligatorias");
            }

            // Obtener serviceArea automáticamente
            String serviceArea = locationService.getServiceArea(seller.getLatitude(), seller.getLongitude());
            seller.setServiceArea(serviceArea);
        // Establecer la fecha de creación si no está presente
        if (seller.getCreatedAt() == null) {
            seller.setCreatedAt(LocalDateTime.now());
        }

        // Convertir Base64 a bytes (si es necesario)
        if (seller.getDniFrontName() != null) {
            seller.setDniFrontName(seller.getDniFrontName());
        }
        if (seller.getDniBackName() != null) {
            seller.setDniBackName(seller.getDniBackName());
        }
        if (seller.getSelfieName() != null) {
            seller.setSelfieName(seller.getSelfieName());
        }
        if (seller.getUniversityTitleName() != null) {
            seller.setUniversityTitleName(seller.getUniversityTitleName());
        }
        if (seller.getCertificationsNames() != null) {
            seller.setCertificationsNames(seller.getCertificationsNames().stream()
                    .map(cert -> cert)
                    .collect(Collectors.toList()));
        }
        if (seller.getGalleryImagesNames() != null) {
            seller.setGalleryImagesNames(seller.getGalleryImagesNames().stream()
                    .map(img -> img)
                    .collect(Collectors.toList()));
        }

        // Guardar o actualizar el seller
            Seller savedSeller = sellerService.updateSellerByUserId(seller.getUserId(), seller);
            return ResponseEntity.ok(savedSeller);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers() {
        List<Seller> sellers = sellerService.getAllSellers();
        return ResponseEntity.ok(sellers);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Seller> updateSeller(@PathVariable Long userId, @RequestBody Seller updatedSeller) {
        Seller seller = sellerService.updateSellerByUserId(userId, updatedSeller);
        return ResponseEntity.ok(seller);
    }

    @GetMapping("/id/{sellerId}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long sellerId) {
        return sellerService.getSellerById(sellerId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/seller/{userId}")
    public ResponseEntity<Seller> getSellerByUserId(@PathVariable Long userId) {
        return sellerService.getSellerByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // En SellerController:
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataError(DataIntegrityViolationException ex) {
        return ResponseEntity.badRequest().body("Error en datos: " + ex.getMessage());
    }
}