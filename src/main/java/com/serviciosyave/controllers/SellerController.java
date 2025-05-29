package com.serviciosyave.controllers;

import com.serviciosyave.dto.SellerDTO;
import com.serviciosyave.entities.Seller;
import com.serviciosyave.entities.Subcategory;
import com.serviciosyave.repositories.SubcategoryRepository;
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
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    public SellerController(SellerService sellerService, LocationService locationService) {
        this.sellerService = sellerService;
        this.locationService = locationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createOrUpdateSeller(@RequestBody SellerDTO sellerDTO){
        try {
            // Validar coordenadas
            if (sellerDTO.getLatitude() == 0 || sellerDTO.getLongitude() == 0) {
                return ResponseEntity.badRequest().body("Coordenadas obligatorias");
            }

            System.out.println("Received subcategory IDs: " + sellerDTO.getSelectedSubcategories());

            // Convertir DTO a entidad Seller
            Seller seller = new Seller();
            seller.setId(sellerDTO.getId());
            seller.setFullName(sellerDTO.getFullName());
            seller.setDniFrontName(sellerDTO.getDniFrontName());
            seller.setDniBackName(sellerDTO.getDniBackName());
            seller.setSelfieName(sellerDTO.getSelfieName());
            seller.setProfilePicture(sellerDTO.getProfilePicture());
            seller.setUniversityTitleName(sellerDTO.getUniversityTitleName());
            seller.setCertificationsNames(sellerDTO.getCertificationsNames());
            seller.setGalleryImagesNames(sellerDTO.getGalleryImagesNames());
            seller.setProfession(sellerDTO.getProfession());
            seller.setYearsOfExperience(sellerDTO.getYearsOfExperience());
            seller.setSkillsDescription(sellerDTO.getSkillsDescription());
            seller.setUserId(sellerDTO.getUserId());
            seller.setServiceArea(sellerDTO.getServiceArea());
            seller.setLatitude(sellerDTO.getLatitude());
            seller.setLongitude(sellerDTO.getLongitude());
            seller.setCoverageRadius(sellerDTO.getCoverageRadius());
            seller.setStatus(sellerDTO.getStatus());
            seller.setCreatedAt(sellerDTO.getCreatedAt());

            // Convertir IDs a entidades Subcategory
            if (sellerDTO.getSelectedSubcategories() != null) {  // Cambiado aquí
                List<Subcategory> subcategories = subcategoryRepository.findAllById(
                        sellerDTO.getSelectedSubcategories()  // Y aquí
                );
                seller.setSelectedSubcategories(subcategories);
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