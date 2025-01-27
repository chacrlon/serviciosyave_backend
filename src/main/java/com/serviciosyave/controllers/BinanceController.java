package com.serviciosyave.controllers;  

import com.serviciosyave.entities.Binance;  
import com.serviciosyave.services.BinanceService;
import com.serviciosyave.services.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.dao.DataAccessException;  
import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;  
import org.springframework.validation.BindingResult;  
import org.springframework.web.bind.annotation.*;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
import java.util.Optional;
import java.util.stream.Collectors;  
import org.springframework.security.core.Authentication;  
import org.springframework.security.core.context.SecurityContextHolder;  
import com.serviciosyave.entities.User;  

@RestController  
@RequestMapping("/api/binance")  
public class BinanceController {  

    @Autowired  
    private BinanceService binanceService;  
    
    @Autowired  
    private JpaUserDetailsService userDetailsService;

    @GetMapping  
    public List<Binance> listar() {  
        return binanceService.getAllBinances();  
    }  

    @GetMapping("/{id}")  
    public ResponseEntity<?> ver(@PathVariable Long id) {  
        Map<String, Object> response = new HashMap<>();  
        Optional<Binance> binance;  

        try {  
            binance = binanceService.getBinanceById(id);  
        } catch (DataAccessException e) {  
            response.put("mensaje", "Error al realizar la consulta en la base de datos");  
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));  
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
        }  

        if (binance.isEmpty()) {  
            response.put("mensaje", "El binance ID: " + id + " no existe en la base de datos!");  
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);  
        }  

        return new ResponseEntity<>(binance.get(), HttpStatus.OK);  
    } 
    
    
    @GetMapping("/user/email")  
    public ResponseEntity<?> getEmailByUserId() {  
        Map<String, Object> response = new HashMap<>();  
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getDetails();  

        User user = userDetailsService.findById(userId); 

        try {  
            Optional<Binance> binanceOpt = binanceService.findBinanceByUserId(userId);  
            
            if (binanceOpt.isPresent()) {  
                String email = binanceOpt.get().getBinanceEmail();  
                response.put("email", email);  
                System.out.println(email);
                return new ResponseEntity<>(response, HttpStatus.OK);  
            } else {  
                response.put("mensaje", "No se encontró el binance para el usuario ID: " + userId);  
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);  
            }  
        } catch (DataAccessException e) {  
            response.put("mensaje", "Error al realizar la consulta en la base de datos");  
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));  
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
        }  
    }
    
    

    @PostMapping("/create") 
    public ResponseEntity<?> crear(@RequestBody Binance binance, BindingResult result) {  
        Map<String, Object> response = new HashMap<>();  
        Binance binanceActual;  

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
            // Buscar el binance existente  
            Optional<Binance> existingBinance = binanceService.findBinanceByUser(user);  

            if (existingBinance.isPresent()) {  
                // Si hay un registro existente, actualizarlo  
                binanceActual = existingBinance.get();  
                binanceActual.setBinanceEmail(binance.getBinanceEmail());  
                binanceService.updateBinance(binanceActual.getId(), binanceActual); // Asegúrate de tener el ID correcto  
                
                response.put("mensaje", "El binance ha sido actualizado con éxito!");  
                response.put("binance", binanceActual);  
                return new ResponseEntity<>(response, HttpStatus.OK);  
            } else {  
                // Si no hay un registro, crearlo  
                binance.setUser(user); // Asocia el usuario logueado  
                binanceActual = binanceService.createBinance(binance);  
                response.put("mensaje", "El binance ha sido creado con éxito!");  
                response.put("binance", binanceActual);  
                return new ResponseEntity<>(response, HttpStatus.CREATED);  
            }  
        } catch (DataAccessException e) {  
            response.put("mensaje", "Error al realizar la operación en la base de datos");  
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));  
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
        }  
    }

    @PutMapping("/{id}")  
    public ResponseEntity<?> actualizar(@RequestBody Binance binance, BindingResult result, @PathVariable Long id) {  
        Map<String, Object> response = new HashMap<>();  
        Binance binanceActual = binanceService.getBinanceById(id).orElse(null);  

        if (result.hasErrors()) {  
            List<String> errors = result.getFieldErrors()  
                    .stream()  
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())  
                    .collect(Collectors.toList());  

            response.put("errors", errors);  
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);  
        }  

        if (binanceActual == null) {  
            response.put("mensaje", "Error: no se pudo editar, el binance ID: " + id + " no existe en la base de datos!");  
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);  
        }  

        try {  
            binanceActual.setBinanceEmail(binance.getBinanceEmail());  
            binanceActual.setVendorService(binance.getVendorService());  
            binanceActual.setUser(binance.getUser());  

            binanceService.updateBinance(id, binanceActual);  

        } catch (DataAccessException e) {  
            response.put("mensaje", "Error al actualizar el binance en la base de datos");  
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));  
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
        }  

        response.put("mensaje", "El binance ha sido actualizado con éxito!");  
        response.put("binance", binanceActual);  

        return new ResponseEntity<>(response, HttpStatus.OK);  
    }  

    @DeleteMapping("/{id}")  
    public ResponseEntity<?> eliminar(@PathVariable Long id) {  
        Map<String, Object> response = new HashMap<>();  

        try {  
            binanceService.deleteBinance(id);  
        } catch (DataAccessException e) {  
            response.put("mensaje", "Error al eliminar el binance de la base de datos");  
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));  
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
        }  

        response.put("mensaje", "El binance ha sido eliminado con éxito!");  
        return new ResponseEntity<>(response, HttpStatus.OK);  
    }  
}