package com.serviciosyave.controllers;

import com.serviciosyave.entities.Ubication;
import com.serviciosyave.services.UbicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200") // Asegúrate de cambiar el origen a tu aplicación Angular
@RestController
@RequestMapping("/api/ubication")
public class UbicationController {

    @Autowired
    private UbicationService ubicationService;

    // Endpoint para recibir la ubicación
    @PostMapping("/ubication")
    public ResponseEntity<Ubication> saveUbication(@RequestBody Ubication ubication) {
        Ubication savedUbication = ubicationService.saveUbication(ubication);
        System.out.println("La ubicacion es : "+ubication);
        return ResponseEntity.ok(savedUbication); // Devuelve 200 OK con la ubicación guardada
    }
}