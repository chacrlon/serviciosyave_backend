package com.serviciosyave.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.serviciosyave.entities.User;
import com.serviciosyave.entities.UserStatus;
import com.serviciosyave.entities.VendorService;
import com.serviciosyave.models.UserRequest;
import com.serviciosyave.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;

@CrossOrigin(origins={"http://localhost:4200"})
@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
    private UserService service;

    @GetMapping
    public List<User> list() {
        return service.findAll();
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