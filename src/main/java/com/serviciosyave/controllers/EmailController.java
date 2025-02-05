package com.serviciosyave.controllers;

import com.serviciosyave.auth.TokenJwtConfig;
import com.serviciosyave.entities.User;
import com.serviciosyave.services.EmailService;

import static com.serviciosyave.auth.TokenJwtConfig.SECRET_KEY;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.serviciosyave.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Optional;

@RestController  
@RequestMapping("/api/email")  
public class EmailController {  

    @Autowired  
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @PostMapping("/send")  
    public ResponseEntity<Map<String, String>> sendEmail(@RequestBody Map<String, String> emailRequest) {  
        String toEmail = emailRequest.get("toEmail");  
        String subject = emailRequest.get("subject");  
        String text = emailRequest.get("text");
        String userType = emailRequest.get("userType"); // Capturamos userType
        
        String[] partes = text.split("/");
        int userId = Integer.parseInt(partes[partes.length - 1]);
        int receiverId= Integer.parseInt(partes[partes.length - 2]);

        // Validar que no sean nulos  
        if (toEmail == null || subject == null || text == null || userType == null) {  
            Map<String, String> errorResponse = new HashMap<>();  
            errorResponse.put("error", "Los campos toEmail, subject, text y userType son requeridos.");  
            return ResponseEntity.badRequest().body(errorResponse);  
        }  

        // Generar un JWT para el receptor  
        String jwt = Jwts.builder()  
                .setSubject(toEmail) // O puedes usar un id de usuario si tienes uno  
                .setIssuedAt(new Date())  
                .setExpiration(new Date(System.currentTimeMillis() + 900000)) // 15 minutos  
                .signWith(TokenJwtConfig.SECRET_KEY) // Usa tu clave secreta  
                .compact();  
        
     // Crear el enlace de chat con el token y userType  
        String chatLink = "http://localhost:4200/chat/invite?userId=" + userId + "&receiverId=" + receiverId + "&token=" + jwt + "&userType=" + userType;  

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
    
    @PostMapping("/auth/token")  
    public ResponseEntity<?> authenticateWithToken(@RequestBody Map<String, String> body) {  
        String token = body.get("token");  
        Claims claims;  
        
        // Generar la clave a partir del secreto  
        //Key key = Keys.hmacShaKeyFor(TokenJwtConfig.SECRET_KEY.getBytes()); // Asegúrate de que sea un tamaño adecuado (32 bytes para HMAC SHA-256)
        Key key = SECRET_KEY; // Usa directamente la clave SECRET_KEY

        try {
            // Utilizar el objeto Key para la validación del token
            claims = Jwts.parser()
                    .setSigningKey(key) // Usamos la clave generada
                    .build()            // Construimos el parser
                    .parseClaimsJws(token) // Analiza el JWT
                    .getBody();
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado");  
        }  
        
        String username = claims.getSubject();
        // Aquí podrías cargar al usuario desde la base de datos y devolver información adicional  
        // Ejemplo:
        Optional<User> user = userService.findByUserEmail(username);
        
        return ResponseEntity.ok(user);
    }
}