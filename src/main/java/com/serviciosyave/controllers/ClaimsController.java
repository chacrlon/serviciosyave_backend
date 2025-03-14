package com.serviciosyave.controllers;

import com.serviciosyave.entities.*;
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
    private IneedService ineedService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<Claims> create(@RequestBody Map<String, String> payloadRequest) {
        try {
            Long userId = Long.valueOf(payloadRequest.get("userId"));
            Long receiverId = Long.valueOf(payloadRequest.get("receiverId"));
            Long vendorServiceId = Long.valueOf(payloadRequest.get("vendorServiceId"));
            Long ineedId = Long.valueOf(payloadRequest.get("ineedId"));
            String roomId = payloadRequest.get("roomId");

            Claims responseClaims;

            Optional<User> user = userService.findById(userId);
            Optional<User> receiver = userService.findById(receiverId);

            if(ineedId != null) {
                Ineed responseIneedService = ineedService.findById(ineedId);
                String observationUser = payloadRequest.get("observationUser");
                String observationReceiver = payloadRequest.get("observationReceiver");

                Claims claims = new Claims(userId, receiverId, roomId, vendorServiceId,ineedId,observationUser,observationReceiver);
                responseClaims = claimsService.save(claims);

                String redirectUrl = "http://localhost:4200/claims/"+responseClaims.getId();
                String subject = "Tienes una Disputa #"+responseClaims.getId()+" por el servicio "+responseIneedService.getTitulo();
                String messageToBuyer = "Se ha creado una disputa, número de seguimiento: "+responseClaims.getId()+" \n\n por el servicio: " + responseIneedService.getTitulo() + " del usuario " + receiver.get().getUsername() + "."+ ".\n\n"+" Completa los datos solicitados en el siguiente enlace: "+redirectUrl;
                String messageToSeller = "El usuario " + user.get().getUsername() + " ha creado una disputa por tu servicio: " + responseIneedService.getTitulo() + ".\n\n"+" Completa los datos solicitados en el siguiente enlace: "+redirectUrl;

                emailService.sendEmail(user.get().getEmail(), subject, messageToBuyer);
                emailService.sendEmail(receiver.get().getEmail(), subject, messageToSeller);

                notificationController.notifyUser(user.get().getId(),receiver.get().getId(), messageToBuyer, "Buyer",null, responseIneedService.getId());
                notificationController.notifyUser(receiver.get().getId(),user.get().getId(), messageToSeller, "Seller",null, responseIneedService.getId());

            } else {
                Optional<VendorService> responseVendorService = vendorService.getServiceById(vendorServiceId);

                Claims claims = new Claims(userId, receiverId, roomId, vendorServiceId,null,null,null);
                responseClaims = claimsService.save(claims);

                String redirectUrl = "http://localhost:4200/claims/"+responseClaims.getId();
                String subject = "Tienes una Disputa #"+responseClaims.getId()+" por el servicio "+responseVendorService.get().getNombre();
                String messageToBuyer = "Se ha creado una disputa, número de seguimiento: "+responseClaims.getId()+" \n\n por el servicio: " + responseVendorService.get().getNombre() + " del usuario " + receiver.get().getUsername() + "."+ ".\n\n"+" Completa los datos solicitados en el siguiente enlace: "+redirectUrl;
                String messageToSeller = "El usuario " + user.get().getUsername() + " ha creado una disputa por tu servicio: " + responseVendorService.get().getNombre() + ".\n\n"+" Completa los datos solicitados en el siguiente enlace: "+redirectUrl;

                emailService.sendEmail(user.get().getEmail(), subject, messageToBuyer);
                emailService.sendEmail(receiver.get().getEmail(), subject, messageToSeller);

                notificationController.notifyUser(user.get().getId(),receiver.get().getId(), messageToBuyer, "Buyer",responseVendorService.get().getId(), null);
                notificationController.notifyUser(receiver.get().getId(),user.get().getId(), messageToSeller, "Seller",responseVendorService.get().getId(), null);

            }

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
            Long ineedServiceId = claim.get().getIneedId();
            String roomId = claim.get().getRoomId();

            Optional<VendorService> responseVendorService = vendorService.getServiceById(vendorServiceId);
            Ineed responseIneedService = ineedService.getIneedById(ineedServiceId);

            List<Payment> respnsePayment = paymentService.findByVendorServiceIdOrIneedIdAndUsersIdAndReceiverId(vendorServiceId, ineedServiceId, userId, receiverId);
            Optional<User> user = userService.findById(userId);
                           user.get().setPassword("");
            Optional<User> userReceiver = userService.findById(receiverId);
                           userReceiver.get().setPassword("");

            Map<String, Object> response = new HashMap<>();
                                response.put("user", user);
                                response.put("receiver", userReceiver);
                                response.put("claim", claim);
                                response.put("vendor", responseVendorService);
                                response.put("ineed", responseIneedService);
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
            @RequestParam("observation_user") String observation_user,
            @RequestParam("observation_receiver") String observation_receiver,
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
            if(user.equals(claim.get().getUserId().toString())) {
                claim.get().setVoucherUser(fileContent);
                claim.get().setObservation_user(observation_user);
            }

            if(user.equals(claim.get().getReceiverId().toString())) {
                claim.get().setVoucherReceiver(fileContent);
                claim.get().setObservation_receiver(observation_receiver);
            }

            Claims latestClaim = claimsService.save(claim.get());

            response.put("claim", latestClaim);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    @PostMapping("/voucherStatus")
    public ResponseEntity<Map<String, Object>> voucherStatus(@RequestBody Map<String, String> payloadRequest) {

        try {
            Map<String, Object> response = new HashMap<>();

            Long longClaimId = Long.valueOf(payloadRequest.get("claimId"));
            Optional<Claims> claim = claimsService.getClaim(longClaimId);
                             claim.get().setStatus(this.getEnumStatus(Integer.parseInt(payloadRequest.get("status"))));
            Claims latestClaim = claimsService.save(claim.get());

            response.put("claim", latestClaim);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    public enum ClaimStatus {
        RESOLVED, PENDING
    }

    private int getEnumStatus(int status) {
        return ClaimStatus.values()[status].ordinal();
    }
}