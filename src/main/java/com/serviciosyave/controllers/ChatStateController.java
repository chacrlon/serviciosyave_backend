package com.serviciosyave.controllers;

import com.serviciosyave.entities.*;
import com.serviciosyave.services.ChatStateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/chat-state")
public class ChatStateController {
    private final ChatStateService service;

    public ChatStateController(ChatStateService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ChatState> saveState(@RequestBody ChatState state) {
        return ResponseEntity.ok(service.saveState(state));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ChatState> getState(@PathVariable Long paymentId) {
        return service.getStateByPaymentId(paymentId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}