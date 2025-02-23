package com.serviciosyave.controllers;

import com.serviciosyave.entities.Claims;
import com.serviciosyave.entities.Payment;
import com.serviciosyave.entities.User;
import com.serviciosyave.entities.VendorService;
import com.serviciosyave.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController  
@RequestMapping("/api/claims")
public class ClaimsController {

    @Autowired  
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private ClaimsService claimsService;

    @Autowired
    private NotificationController notificationController;

    @Autowired
    private VendorServiceService vendorService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<Claims> create(@RequestBody Map<String, String> payloadRequest) {
        try {
            Long userId = Long.valueOf(payloadRequest.get("userId"));
            Long receiverId = Long.valueOf(payloadRequest.get("receiverId"));
            Long vendorServiceId = Long.valueOf(payloadRequest.get("vendorServiceId"));
            String roomId = payloadRequest.get("roomId");

            Optional<VendorService> responseVendorService = vendorService.getServiceById(vendorServiceId);
            Optional<User> user = userService.findById(userId);
            Optional<User> receiver = userService.findById(receiverId);

            Claims claims = new Claims(userId, receiverId, roomId, vendorServiceId);
            Claims responseClaims = claimsService.save(claims);

            String redirectUrl = "http://localhost:4200/claims/"+responseClaims.getId();
            String subject = "Tienes una Disputa #"+responseClaims.getId()+" por el servicio "+responseVendorService.get().getNombre();
            String messageToBuyer = "Se ha creado una disputa, número de seguimiento: "+responseClaims.getId()+" \n\n por el servicio: " + responseVendorService.get().getNombre() + " del usuario " + receiver.get().getUsername() + "."+ ".\n\n"+" Completa los datos solicitados en el siguiente enlace: "+redirectUrl;
            String messageToSeller = "El usuario " + user.get().getUsername() + " ha creado una disputa por tu servicio: " + responseVendorService.get().getNombre() + ".\n\n"+" Completa los datos solicitados en el siguiente enlace: "+redirectUrl;

            emailService.sendEmail(user.get().getEmail(), subject, messageToBuyer);
            emailService.sendEmail(receiver.get().getEmail(), subject, messageToSeller);

            notificationController.notifyUser(user.get().getId(),receiver.get().getId(), messageToBuyer, "Buyer",responseVendorService.get().getId());
            notificationController.notifyUser(receiver.get().getId(),user.get().getId(), messageToSeller, "Seller",responseVendorService.get().getId());

            return ResponseEntity.ok(responseClaims);
        } catch (Exception e) {
            return (ResponseEntity<Claims>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getClaim")
    public ResponseEntity<Map<String, Object>> getClaim(@RequestBody Map<String, String> payloadRequest) {
        try {
            Long claimId = Long.valueOf(payloadRequest.get("claimId"));
            Optional<Claims> claim = claimsService.getClaim(claimId);

            Long userId = claim.get().getUserId();
            Long receiverId = claim.get().getReceiverId();
            Long vendorServiceId = claim.get().getVendorServiceId();
            String roomId = claim.get().getRoomId();

            Optional<VendorService> responseVendorService = vendorService.getServiceById(vendorServiceId);
            List<Payment> respnsePayment = paymentService.findByVendorServiceIdAndUserId(vendorServiceId, userId);
            Optional<User> user = userService.findById(userId);
                           user.get().setPassword("");
            Optional<User> userReceiver = userService.findById(receiverId);
                           userReceiver.get().setPassword("");

            Map<String, Object> response = new HashMap<>();
                                response.put("user", user);
                                response.put("receiver", userReceiver);
                                response.put("claim", claim);
                                response.put("vendor", responseVendorService);
                                response.put("payment", respnsePayment);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    @PostMapping("/voucher")
    public ResponseEntity<Map<String, Object>> getClaim(
            @RequestPart("file") MultipartFile file,
            @RequestParam("user") String user,
            @RequestParam("claimId") String claimId) {

        try {
            Map<String, Object> response = new HashMap<>();

            if (file.isEmpty()) {
                response.put("Error", "El archivo está vacío");
                return ResponseEntity.badRequest().body(response);
            }

            String fileName = file.getOriginalFilename();
            long fileSize = file.getSize();
            byte[] fileContent = file.getBytes();

            Long longClaimId = Long.valueOf(claimId);
            Optional<Claims> claim = claimsService.getClaim(longClaimId);
            if(user.equals(claim.get().getUserId().toString())) { claim.get().setVoucherUser(fileContent); }
            if(user.equals(claim.get().getReceiverId().toString())) { claim.get().setVoucherReceiver(fileContent); }
            Claims latestClaim = claimsService.save(claim.get());

            response.put("claim", latestClaim);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }
}