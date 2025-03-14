package com.serviciosyave.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.serviciosyave.entities.PaymentMethodSelected;
import com.serviciosyave.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.serviciosyave.entities.User;
import com.serviciosyave.entities.UserStatus;
import com.serviciosyave.entities.VendorService;
import com.serviciosyave.models.UserRequest;
import com.serviciosyave.services.UserService;

import jakarta.validation.Valid;

@CrossOrigin(origins = {"http://localhost:4200"}) // Configuración de CORS a nivel de clase
@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
    private UserService service;

    @Autowired
    private UserRepository userRepository; // Añadir esta línea

    @GetMapping
    public List<User> list() {
        return service.findAll();
    }

    @PatchMapping("/payment-method") // Cambiado a @PatchMapping directo
    public ResponseEntity<?> updatePaymentMethod(@RequestBody Map<String, String> request) {
        String paymentMethod = request.get("paymentMethod");

        // Obtener el nombre de usuario del contexto de seguridad
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Buscar al usuario por nombre de usuario
        Optional<User> userOptional = userRepository.findByUsername(username);

        // Verificar si el usuario está presente
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            try {
                user.setPaymentMethodSelected(PaymentMethodSelected.valueOf(paymentMethod));
                userRepository.save(user);
                return ResponseEntity.ok().build();
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Método de pago inválido");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Usuario no encontrado."));
        }
    }

    @GetMapping("/{userId}/email")  
    public ResponseEntity<?> getUserEmail(@PathVariable Long userId) {  
        Optional<User> userOptional = service.findById(userId);  
        if (userOptional.isPresent()) {  
            String email = userOptional.get().getEmail();  
            return ResponseEntity.ok(Collections.singletonMap("email", email));  
        }  
        return ResponseEntity.status(HttpStatus.NOT_FOUND)  
                .body(Collections.singletonMap("error", "Usuario no encontrado con el ID: " + userId));  
    }

    @GetMapping("/page/{page}")
    public Page<User> listPageable(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<User> userOptional = service.findById(id);
        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(userOptional.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("error", "el usuario no se encontro por el id:" + id));
    }
    
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    private static final double PROMEDIO = 3.0;
    private static final double FACTOR_AJUSTE = 0.5;

    @PostMapping("/valorate")
    public ResponseEntity<?> valorate(@RequestBody Map<String, String> payloadRequest) {
        Long receiverId = Long.valueOf(payloadRequest.get("receiverId"));
        Double rating = Double.valueOf(payloadRequest.get("rating"));
        User receiverUser = userRepository.findById(receiverId).get();

        Double latestRating = receiverUser.getRating();
        double difference = rating - PROMEDIO;
        double fix = difference * FACTOR_AJUSTE;
        double newRating = latestRating + fix;

             receiverUser.setRating(newRating);
        User receiverSaved = userRepository.save(receiverUser);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody UserRequest user, BindingResult result, @PathVariable Long id) {

        if (result.hasErrors()) {
            return validation(result);
        }
        
        Optional<User> userOptional = service.update(user, id);

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<User> userOptional = service.findById(id);
        if (userOptional.isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    
    @PostMapping("/{userId}")  
    public String verifyUser(@PathVariable Long userId,  
                             @RequestBody String verificationCode) {  
        Optional<User> optionalUser = service.findById(userId); // Obtener el usuario por su ID  

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
    
    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
    
    
    @GetMapping("/{userId}/services")  
    public ResponseEntity<?> getServicesByUserId(@PathVariable Long userId) {  
        List<VendorService> services = service.findServicesByUserId(userId);  
        if (services.isEmpty()) {  
            return ResponseEntity.status(HttpStatus.NOT_FOUND)  
                    .body(Collections.singletonMap("error", "No se encontraron servicios para el usuario con ID: " + userId));  
        }  
        return ResponseEntity.ok(services);  
    }
    
    @PutMapping("/{id}/status/ocupado")  
    public ResponseEntity<?> setUserStatusOcupado(@PathVariable Long id) {  
        Optional<User> updatedUser = service.updateUserStatus(id, UserStatus.OCUPADO);  
        if (updatedUser.isPresent()) {  
            return ResponseEntity.ok(updatedUser.get());  
        }  
        return ResponseEntity.status(HttpStatus.NOT_FOUND)  
                .body(Collections.singletonMap("error", "Usuario no encontrado con ID: " + id));  
    }  

    @PutMapping("/{id}/status/no-ocupado")  
    public ResponseEntity<?> setUserStatusNoOcupado(@PathVariable Long id) {  
        Optional<User> updatedUser = service.updateUserStatus(id, UserStatus.NO_OCUPADO);  
        if (updatedUser.isPresent()) {  
            return ResponseEntity.ok(updatedUser.get());  
        }  
        return ResponseEntity.status(HttpStatus.NOT_FOUND)  
                .body(Collections.singletonMap("error", "Usuario no encontrado con ID: " + id));  
    }
}