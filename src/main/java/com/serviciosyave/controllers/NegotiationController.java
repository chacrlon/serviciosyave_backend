package com.serviciosyave.controllers;

import com.serviciosyave.dto.NegotiationDTO;
import com.serviciosyave.entities.Negotiate;
import com.serviciosyave.services.NegotiationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/negotiations")
public class NegotiationController {

    @Autowired
    private NegotiationService negotiationService;

    @PostMapping
    public ResponseEntity<?> createNegotiation(@RequestBody NegotiationDTO dto) {
        try {
            return ResponseEntity.ok(negotiationService.createNegotiation(dto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<?> acceptNegotiation(@PathVariable Long id) {
        try {
            negotiationService.acceptNegotiation(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}