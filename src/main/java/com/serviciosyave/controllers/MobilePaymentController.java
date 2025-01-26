package com.serviciosyave.controllers;

import com.serviciosyave.entities.MobilePayment;
import com.serviciosyave.services.JpaUserDetailsService;
import com.serviciosyave.services.MobilePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataAccessException;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.serviciosyave.entities.User;

@RestController  
@RequestMapping("/api/mobilepayment")  
public class MobilePaymentController {  

    @Autowired  
    private MobilePaymentService mobilePaymentService;  
    
    @Autowired  
    private JpaUserDetailsService userDetailsService;
    
    
    @GetMapping("/user")  
    public ResponseEntity<?> getMobilePaymentByUserId() {  
        Map<String, Object> response = new HashMap<>();  

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  
        Long userId = (Long) authentication.getDetails();  

        User user = userDetailsService.findById(userId);  

        try {  
            // Obtener el pago móvil del usuario  
            Optional<MobilePayment> mobilePayment = mobilePaymentService.findMobilePaymentByUser(user);

            if (mobilePayment.isPresent()) {  
                response.put("mobilePayment", mobilePayment.get());  
                return new ResponseEntity<>(response, HttpStatus.OK);  
            } else {  
                response.put("mensaje", "No se encontró un pago móvil para el usuario ID: " + userId);  
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);  
            }  
        } catch (DataAccessException e) {  
            response.put("mensaje", "Error al realizar la consulta en la base de datos");  
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));  
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
        }  
    }  

    @PostMapping("/create")  
    public ResponseEntity<?> createOrUpdateMobilePayment(@Valid @RequestBody MobilePayment mobilePayment, BindingResult result) {  
        Map<String, Object> response = new HashMap<>();  

        if (result.hasErrors()) {  
            List<String> errors = result.getFieldErrors()  
                    .stream()  
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())  
                    .collect(Collectors.toList());  

            response.put("errors", errors);  
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);  
        }  

        try {  
            // Obtener el usuario actualmente autenticado  
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  
            Long userId = (Long) authentication.getDetails();  

            User user = userDetailsService.findById(userId);  

            // Verificar si ya existen registros de MobilePayment para este usuario  
 Optional<MobilePayment> existingMobilePaymentOpt = mobilePaymentService.findMobilePaymentByUserId(userId);  

            if (existingMobilePaymentOpt.isPresent()) {  
                // Si existe un registro, actualizarlo  
                MobilePayment existingMobilePayment = existingMobilePaymentOpt.get();  
                existingMobilePayment.setCedula(mobilePayment.getCedula());  
                existingMobilePayment.setNumeroTelefono(mobilePayment.getNumeroTelefono());  
                existingMobilePayment.setBanco(mobilePayment.getBanco());  

                // Guardar el registro actualizado  
                MobilePayment updatedMobilePayment = mobilePaymentService.updateMobilePayment(existingMobilePayment.getId(), existingMobilePayment);  
                response.put("mensaje", "El pago móvil ha sido actualizado con éxito!");  
                response.put("mobilePayment", updatedMobilePayment);  
                return new ResponseEntity<>(response, HttpStatus.OK);  
            } else {  
                // Si no existe un registro, proceder a crear uno nuevo  
                mobilePayment.setUser(user); // Asocia el usuario autenticado al pago móvil  
                MobilePayment createdMobilePayment = mobilePaymentService.createMobilePayment(mobilePayment);  
                response.put("mensaje", "El pago móvil ha sido creado con éxito!");  
                response.put("mobilePayment", createdMobilePayment);  
                return new ResponseEntity<>(response, HttpStatus.CREATED);  
            }  
        } catch (DataAccessException e) {  
            response.put("mensaje", "Error al realizar la operación en la base de datos");  
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));  
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
        }  
    }  

    @GetMapping("/")  
    public ResponseEntity<?> getAllMobilePayments() {  
        Map<String, Object> response = new HashMap<>();  
        List<MobilePayment> mobilePayments;  

        try {  
            mobilePayments = mobilePaymentService.getAllMobilePayments();  
        } catch (DataAccessException e) {  
            response.put("mensaje", "Error al realizar la consulta en la base de datos");  
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));  
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
        }  

        response.put("mobilePayments", mobilePayments);  
        return new ResponseEntity<>(response, HttpStatus.OK);  
    }  

    @GetMapping("/{id}")  
    public ResponseEntity<?> getMobilePaymentById(@PathVariable Long id) {  
        Map<String, Object> response = new HashMap<>();  
        Optional<MobilePayment> mobilePaymentOpt;  

        try {  
            mobilePaymentOpt = mobilePaymentService.getMobilePaymentById(id);  
        } catch (DataAccessException e) {  
            response.put("mensaje", "Error al realizar la consulta en la base de datos");  
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));  
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
        }  

        if (!mobilePaymentOpt.isPresent()) {  
            response.put("mensaje", "Error: el mobile payment ID: " + id + " no existe en la base de datos!");  
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);  
        }  

        response.put("mobilePayment", mobilePaymentOpt.get());  
        return new ResponseEntity<>(response, HttpStatus.OK);  
    }  

    @PutMapping("/{id}")  
    public ResponseEntity<?> updateMobilePayment(@PathVariable Long id, @Valid @RequestBody MobilePayment mobilePaymentDetails, BindingResult result) {  
        Map<String, Object> response = new HashMap<>();  
        Optional<MobilePayment> currentMobilePaymentOpt = mobilePaymentService.getMobilePaymentById(id);  

        // Validación de errores  
        if (result.hasErrors()) {  
            List<String> errors = result.getFieldErrors()  
                    .stream()  
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())  
                    .collect(Collectors.toList());  
            response.put("errors", errors);  
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);  
        }  

        if (!currentMobilePaymentOpt.isPresent()) {  
            response.put("mensaje", "Error: no se pudo editar, el mobile payment ID: " + id + " no existe en la base de datos!");  
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);  
        }  

        MobilePayment currentMobilePayment = currentMobilePaymentOpt.get();  
        
        try {  
            // Actualizar los campos  
            currentMobilePayment.setCedula(mobilePaymentDetails.getCedula());  
            currentMobilePayment.setNumeroTelefono(mobilePaymentDetails.getNumeroTelefono());  
            currentMobilePayment.setBanco(mobilePaymentDetails.getBanco());  
            currentMobilePayment.setVendorService(mobilePaymentDetails.getVendorService());  
            currentMobilePayment.setUser(mobilePaymentDetails.getUser());  
            
            mobilePaymentService.updateMobilePayment(id, currentMobilePayment);  
        } catch (DataAccessException e) {  
            response.put("mensaje", "Error al actualizar el mobile payment en la base de datos");  
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));  
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
        }  

        response.put("mensaje", "El mobile payment ha sido actualizado con éxito!");  
        response.put("mobilePayment", currentMobilePayment);  
        return new ResponseEntity<>(response, HttpStatus.OK);  
    }  

    @DeleteMapping("/{id}")  
    public ResponseEntity<?> deleteMobilePayment(@PathVariable Long id) {  
        Map<String, Object> response = new HashMap<>();  

        try {  
            Optional<MobilePayment> mobilePaymentOpt = mobilePaymentService.getMobilePaymentById(id);  
            if (!mobilePaymentOpt.isPresent()) {  
                response.put("mensaje", "Error: el mobile payment ID: " + id + " no existe en la base de datos!");  
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);  
            }  

            mobilePaymentService.deleteMobilePayment(id);  
        } catch (DataAccessException e) {  
            response.put("mensaje", "Error al eliminar el mobile payment de la base de datos");  
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));  
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
        }  

        response.put("mensaje", "El mobile payment ha sido eliminado con éxito!");  
        return new ResponseEntity<>(response, HttpStatus.OK);  
    }  
}