package com.springboot.backend.andres.usersapp.usersbackend.controllers;  

import com.springboot.backend.andres.usersapp.usersbackend.services.EmailService;  
import java.util.HashMap;  
import java.util.Map;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;  
import org.springframework.web.bind.annotation.*;  

@RestController  
@RequestMapping("/api/email")  
public class EmailController {  

    @Autowired  
    private EmailService emailService;  

    @PostMapping("/send")  
    public ResponseEntity<Map<String, String>> sendEmail(@RequestBody Map<String, String> emailRequest) {  
        String toEmail = emailRequest.get("toEmail");  
        String subject = emailRequest.get("subject");  
        String text = emailRequest.get("text");  

        // Validar que no sean nulos  
        if (toEmail == null || subject == null || text == null) {  
            Map<String, String> errorResponse = new HashMap<>();  
            errorResponse.put("error", "Los campos toEmail, subject y text son requeridos.");  
            return ResponseEntity.badRequest().body(errorResponse);  
        }  

        boolean isSent = emailService.sendEmail(toEmail, subject, text);  
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