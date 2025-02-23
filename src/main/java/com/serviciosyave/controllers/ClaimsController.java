package com.serviciosyave.controllers;

import com.serviciosyave.auth.TokenJwtConfig;
import com.serviciosyave.entities.Claims;
import com.serviciosyave.entities.Payment;
import com.serviciosyave.entities.User;
import com.serviciosyave.entities.VendorService;
import com.serviciosyave.services.*;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            Claims responseClaims = claimsService.create(claims);

            String redirectUrl = "http://localhost:4200/claims/"+responseClaims.getId();
            String subject = "Tienes una Disputa #"+responseClaims.getId()+" por el servicio "+responseVendorService.get().getNombre();
            String messageToBuyer = "Se ha creado una disputa, número de seguimiento: "+responseClaims.getId()+" \n\n por el servicio: " + responseVendorService.get().getNombre() + " del usuario " + receiver.get().getUsername() + "."+ ".\n\n"+" Completa los datos solicitados en el siguiente enlace: "+redirectUrl;
            String messageToSeller = "El usuario " + user.get().getUsername() + " ha creado una disputa por tu servicio: " + responseVendorService.get().getNombre() + ".\n\n"+" Completa los datos solicitados en el siguiente enlace: "+redirectUrl;

            emailService.sendEmail(user.get().getEmail(), subject, messageToBuyer);
            emailService.sendEmail(receiver.get().getEmail(), subject, messageToSeller);

            notificationController.notifyUser(user.get().getId(),receiver.get().getId(), messageToBuyer, responseVendorService.get().getId());
            notificationController.notifyUser(receiver.get().getId(),user.get().getId(), messageToSeller, responseVendorService.get().getId());

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

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendEmail(@RequestBody Map<String, String> emailRequest) {  
        String toEmail = emailRequest.get("toEmail");  
        String subject = emailRequest.get("subject");  
        String text = emailRequest.get("text");

        String[] partes = text.split("/");
        int userId = Integer.parseInt(partes[partes.length - 1]);
        int receiverId= Integer.parseInt(partes[partes.length - 2]);

        // Validar que no sean nulos
        if (toEmail == null || subject == null || text == null) {  
            Map<String, String> errorResponse = new HashMap<>();  
            errorResponse.put("error", "Los campos toEmail, subject y text son requeridos.");  
            return ResponseEntity.badRequest().body(errorResponse);  
        }  

        // Generar un JWT para el receptor  
        String jwt = Jwts.builder()  
                .setSubject(toEmail) // O puedes usar un id de usuario si tienes uno  
                .setIssuedAt(new Date())  
                .setExpiration(new Date(System.currentTimeMillis() + 900000)) // 15 minutos  
                .signWith(TokenJwtConfig.SECRET_KEY) // Usa tu clave secreta  
                .compact();  
        
        // Crear el enlace de chat con el token  
        String chatLink = "http://localhost:4200/chat/invite?userId="+userId+"&receiverId="+receiverId+"&token=" + jwt; // Cambia la ruta según tu diseño

        // Enviar el correo con el enlace de chat  
        boolean isSent = emailService.sendEmail(toEmail, subject, "Haz clic aquí para unirte al chat: " + chatLink);  
        Map<String, String> response = new HashMap<>();  
        if (isSent) {  
            response.put("message", "Email enviado con éxito");  
            return ResponseEntity.ok(response);  
        } else {  
            response.put("error", "Error al enviar el email");  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);  
        }  
    }    

}