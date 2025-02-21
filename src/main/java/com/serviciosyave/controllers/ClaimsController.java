package com.serviciosyave.controllers;

import com.serviciosyave.auth.TokenJwtConfig;
import com.serviciosyave.entities.Claims;
import com.serviciosyave.entities.User;
import com.serviciosyave.services.ClaimsService;
import com.serviciosyave.services.EmailService;
import com.serviciosyave.services.UserService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController  
@RequestMapping("/api/claims")
public class ClaimsController {

    @Autowired  
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private ClaimsService claimsService;

    @PostMapping("/create")
    public ResponseEntity<Claims> create(@RequestBody Map<String, String> payloadRequest) {
        try {
            Long userId = Long.valueOf(payloadRequest.get("userId"));
            Long receiverId = Long.valueOf(payloadRequest.get("receiverId"));
            Long vendorServiceId = Long.valueOf(payloadRequest.get("vendorServiceId"));
            String roomId = payloadRequest.get("roomId");

            Optional<User> user = userService.findById(userId);

            Claims claims = new Claims(userId, receiverId, roomId, vendorServiceId);
            Claims responseClaims = claimsService.create(claims);

            return ResponseEntity.ok(responseClaims);
        } catch (Exception e) {
            return (ResponseEntity<Claims>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
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