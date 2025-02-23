package com.serviciosyave.controllers;   

import com.serviciosyave.Enum.NegotiationStatus;
import com.serviciosyave.repositories.NegotiateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;  
import org.springframework.web.bind.annotation.*;
import com.serviciosyave.dto.AcceptOfferRequest;
import com.serviciosyave.entities.Ineed;
import com.serviciosyave.repositories.IneedRepository;
import com.serviciosyave.services.IneedService;
import java.util.List;
import java.util.Optional;   

@RestController  
@RequestMapping("/api/ineeds")  
public class IneedController {  

	private final IneedService ineedService;  
 
    public IneedController(IneedService ineedService) {  
        this.ineedService = ineedService;  
    }

    @PostMapping  
    public ResponseEntity<Ineed> crearNecesidad(@RequestBody Ineed ineed) {  
        Ineed nuevaNecesidad = ineedService.crearNecesidad(ineed);  
        return new ResponseEntity<>(nuevaNecesidad, HttpStatus.CREATED);  
    }  

    @GetMapping  
    public ResponseEntity<List<Ineed>> obtenerNecesidades() {  
        List<Ineed> necesidades = ineedService.obtenerNecesidades();  
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