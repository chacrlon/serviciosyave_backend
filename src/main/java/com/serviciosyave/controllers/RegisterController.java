package com.serviciosyave.controllers;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.serviciosyave.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.serviciosyave.entities.User;
import com.serviciosyave.services.EmailService;
import com.serviciosyave.services.UserService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService service;

    @Autowired
    private EmailService emailservice;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registro de usuario
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }

        // Generar código de verificación y asignarlo
        generateAndAssignVerificationCode(user);

        // Guardar usuario en la base de datos
        User savedUser = service.save(user);

        // Enviar correo electrónico de verificación
        String emailSubject = "Código de Verificación";
        String emailText = "Por favor verifica tu correo utilizando este código: " + user.getVerificationCode();

        boolean emailSent = emailservice.sendEmail(savedUser.getEmail(), emailSubject, emailText);

        if (!emailSent) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error al enviar el correo de verificación."));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // Endpoint para reenviar el código de verificación
    @PostMapping("/resend-code/{id}")
    public ResponseEntity<?> resendVerificationCode(@PathVariable Long id) {
        Optional<User> optionalUser = service.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Generar un nuevo código de verificación y asignarlo
            generateAndAssignVerificationCode(user);

            // Guardar el usuario actualizado en la base de datos
            service.save(user);

            // Enviar correo electrónico con el nuevo código
            String emailSubject = "Nuevo Código de Verificación";
            String emailText = "Por favor verifica tu correo utilizando este código, vence en 30 minutos: " + user.getVerificationCode();

            boolean emailSent = emailservice.sendEmail(user.getEmail(), emailSubject, emailText);

            if (!emailSent) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("error", "Error al enviar el correo de verificación."));
            }

            return ResponseEntity.ok("Nuevo código de verificación enviado.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }
    }

    // Método para verificar el código de verificación
    @PostMapping("/code/{id}")
    public ResponseEntity<String> verifyUser(
            @PathVariable Long id,
            @RequestBody Map<String, String> body // Cambia el nombre del campo aquí
    ) {
        // Usa "verificationCode" en lugar de "isEmailVerified"
        String verificationCode = body.get("verificationCode"); // <-- Corregir aquí
        Optional<User> optionalUser = service.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Verificar si el código ha expirado
            if (user.getVerificationCodeExpiry() != null && LocalDateTime.now().isAfter(user.getVerificationCodeExpiry())) {
                return ResponseEntity.badRequest().body("El código de verificación ha expirado.");
            }

            // Verifica el código
            if (user.getVerificationCode().equals(verificationCode)) {
                user.setIsEmailVerified(true);
                service.save(user);
                return ResponseEntity.ok("Email verificado con éxito.");
            } else {
                return ResponseEntity.badRequest().body("El código de verificación es incorrecto.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }
    }

    // Método privado para generar y asignar un código de verificación con fecha de expiración
    private void generateAndAssignVerificationCode(User user) {
        String verificationCode = UUID.randomUUID().toString();
        user.setVerificationCode(verificationCode);
        user.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(30)); // Código válido por 30 minutos
        user.setIsEmailVerified(false);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            service.processForgotPassword(request.get("username"));
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la solicitud");
        }
    }

    // Validar token de reset
    @GetMapping("/validate-reset-token")
    public ResponseEntity<?> validateResetToken(@RequestParam String token) {
        Optional<User> user = userRepository.findByResetToken(token);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token inválido");
        }

        if (user.get().getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expirado");
        }

        return ResponseEntity.ok().build();
    }

    // Actualizar contraseña
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPasswordHandler(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        Optional<User> userOptional = userRepository.findByResetToken(token);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token inválido");
        }

        User user = userOptional.get();

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expirado");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        return ResponseEntity.ok("Contraseña actualizada");
    }

    // Método para manejar errores de validación
    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}