package com.springboot.backend.andres.usersapp.usersbackend.controllers;  

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;  
import java.util.HashMap;  
import java.util.Map;
import java.util.Optional;
import java.util.UUID;  

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;  
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;  
import org.springframework.web.bind.annotation.RequestBody;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  

import com.springboot.backend.andres.usersapp.usersbackend.entities.User;  
import com.springboot.backend.andres.usersapp.usersbackend.services.EmailService;  
import com.springboot.backend.andres.usersapp.usersbackend.services.UserService;  

import jakarta.validation.Valid;  

@RestController  
@RequestMapping("/register")  
public class RegisterController {  

    @Autowired  
    private UserService service;  
	  
    @Autowired  
    private EmailService emailservice;  
    
    @PostMapping("/register")  
    public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult result) {  
        if (result.hasErrors()) {  
            return validation(result);  
        }  

        // Validar edad para asegurarse de que no es menor de 18 años
        /*
        if (!isAdult(user.getDateOfBirth())) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)  
                .body(Collections.singletonMap("error", "Debes tener al menos 18 años para registrarte."));  
        }  
        */
        // Generar código de verificación y asignarlo  
        String verificationCode = UUID.randomUUID().toString();   
        user.setVerificationCode(verificationCode);  
        user.setIsEmailVerified(false);   
        
        // Guardar usuario en la base de datos  
        User savedUser = service.save(user);   

        // Enviar correo electrónico de verificación  
        String emailSubject = "Código de Verificación";  
        String emailText = "Por favor verifica tu correo utilizando este código: " + verificationCode;  
        
        boolean emailSent = emailservice.sendEmail(savedUser.getEmail(), emailSubject, emailText);  

        if (!emailSent) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)  
                .body(Collections.singletonMap("error", "Error al enviar el correo de verificación."));  
        }  

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);  
    }  
    
    private boolean isAdult(LocalDate dateOfBirth) {  
        LocalDate today = LocalDate.now();  
        return dateOfBirth != null && Period.between(dateOfBirth, today).getYears() >= 18;  
    }  
    
    @GetMapping("/code/{id}") 
    public String verifyUser(@PathVariable Long id,  
                             @RequestBody String verificationCode) {  
        Optional<User> optionalUser = service.findById(id); // Obtener el usuario por su ID  

        if (optionalUser.isPresent()) {  
            User user = optionalUser.get(); // Obtener el usuario del Optional  

            // Verifica el código de verificación  
            if (user.getVerificationCode().equals(verificationCode)) {  
                user.setIsEmailVerified(true); // Cambia el estado de verificación a true  
                service.save(user); // Guarda los cambios en el usuario  
                return "Email verificado con éxito.";  
            } else {  
                return "El código de verificación es incorrecto.";  
            }  
        } else {  
            return "Usuario no encontrado.";  
        }  
    }
    
    // Este método se puede llevar a otra clase para una mejor organización  
    private ResponseEntity<?> validation(BindingResult result) {  
        Map<String, String> errors = new HashMap<>();  
        result.getFieldErrors().forEach(error -> {  
            errors.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());  
        });  
        return ResponseEntity.badRequest().body(errors);  
    }  
}