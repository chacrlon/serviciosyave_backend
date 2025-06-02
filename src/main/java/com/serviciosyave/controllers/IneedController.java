package com.serviciosyave.controllers;   

import com.serviciosyave.Enum.NegotiationStatus;
import com.serviciosyave.entities.User;
import com.serviciosyave.repositories.NegotiateRepository;
import com.serviciosyave.repositories.UserRepository;
import com.serviciosyave.services.EmailService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.serviciosyave.dto.AcceptOfferRequest;
import com.serviciosyave.entities.Ineed;
import com.serviciosyave.repositories.IneedRepository;
import com.serviciosyave.services.IneedService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController  
@RequestMapping("/api/ineeds")  
public class IneedController {  

	private final IneedService ineedService;  
 
    public IneedController(IneedService ineedService) {  
        this.ineedService = ineedService;  
    }
    @Autowired  
    private IneedRepository ineedRepository; 
    
    @Autowired  
    private NotificationController notificationController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/{ineedId}/accept/{providerId}")
    public ResponseEntity<Map<String, String>> acceptIneed(
            @PathVariable Long ineedId,
            @PathVariable Long providerId
    ) {
        Ineed ineed = ineedService.findById(ineedId);
        User provider = userRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        User buyer = userRepository.findById(ineed.getUserId())
                .orElseThrow(() -> new RuntimeException("Comprador no encontrado"));

        // 2. Crear notificación
        String message = String.format(
                "Tu requerimiento '%s' ha sido aceptado por %s",
                ineed.getTitulo(),
                provider.getUsername()
        );

        Double ineedAmount = ineed.getPresupuesto();
        notificationController.notifyUser(
                buyer.getId(),
                provider.getId(),
                message,
                "Buyer",
                null,
                ineedId,
                "requerimiento",
                "no_pagado",
                ineedAmount,
                null);

        // 3. Enviar email
        emailService.sendEmail(
                buyer.getEmail(),
                "Requerimiento Aceptado",
                message
        );

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Collections.singletonMap("message", "Notificación creada exitosamente"));
    }

    @PostMapping("/aceptar")  
    public ResponseEntity<Ineed> aceptarOferta(@RequestBody AcceptOfferRequest request) {  
        Optional<Ineed> necesidadOpt = ineedRepository.findById(request.getNecesidadId());  

        if (necesidadOpt.isPresent()) {  
            Ineed necesidad = necesidadOpt.get();  
            necesidad.setProfessionalUserId(request.getProfessionalUserId());  
            Ineed updatedNecesidad = ineedRepository.save(necesidad);
            // Obtener el monto del requerimiento
            Double montoRequerimiento = necesidad.getPresupuesto();
            // Crear y enviar la notificación al cliente  
            String message = "El profesional ha aceptado tu oferta para: " + necesidad.getTitulo();  
            notificationController.notifyUser(necesidad.getUserId(),
                    request.getProfessionalUserId(),
                    message,
                    null,
                    necesidad.getId(),
                    null,
                    "requerimiento",
                    "no_pagado",
                    montoRequerimiento,
                    null); // Aquí se envía la notificación

            return ResponseEntity.ok(updatedNecesidad);  
        } else {  
            return ResponseEntity.notFound().build();  
        }  
    }

    @PostMapping
    public ResponseEntity<Ineed> crearNecesidad(@RequestBody Ineed ineed) {
        try {
            Ineed nuevaNecesidad = ineedService.crearNecesidad(ineed);
            return new ResponseEntity<>(nuevaNecesidad, HttpStatus.CREATED);
        } catch (ValidationException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping  
    public ResponseEntity<List<Ineed>> obtenerNecesidades(@RequestParam Double lat, @RequestParam Double lon) {
        List<Ineed> necesidades = ineedService.obtenerNecesidades(lat, lon);
        return new ResponseEntity<>(necesidades, HttpStatus.OK);
    }

    @GetMapping("/{id}")  
    public ResponseEntity<Ineed> obtenerNecesidadPorId(@PathVariable Long id) {  
        Ineed necesidad = ineedService.obtenerNecesidadPorId(id);  
        return necesidad != null ? new ResponseEntity<>(necesidad, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);  
    }  

    @DeleteMapping("/{id}")  
    public ResponseEntity<Void> eliminarNecesidad(@PathVariable Long id) {  
        ineedService.eliminarNecesidad(id);  
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  
    }  
}