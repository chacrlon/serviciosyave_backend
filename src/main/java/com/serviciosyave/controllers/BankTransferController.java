package com.serviciosyave.controllers;

import com.serviciosyave.entities.BankTransfer;
import com.serviciosyave.entities.User;
import com.serviciosyave.services.BankTransferService;
import com.serviciosyave.services.JpaUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController  
@RequestMapping("/api/banktransfer")  
public class BankTransferController {  

    @Autowired  
    private BankTransferService bankTransferService; 
    
    @Autowired  
    private JpaUserDetailsService userDetailsService;
    
    
    @GetMapping("/user")  
    public ResponseEntity<?> getBankTransferByUserId() {  
        Map<String, Object> response = new HashMap<>();  

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  
        Long userId = (Long) authentication.getDetails();  

        User user = userDetailsService.findById(userId);  

        try {  
            // Obtener la lista de transferencias bancarias del usuario  
        	Optional<BankTransfer> bankTransfers = bankTransferService.findBankTransferByUser(user);

            if (!bankTransfers.isEmpty()) {  
                response.put("bankTransfers", bankTransfers);  
                return new ResponseEntity<>(response, HttpStatus.OK);  
            } else {  
                response.put("mensaje", "No se encontraron transferencias bancarias para el usuario ID: " + userId);  
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);  
            }  
        } catch (DataAccessException e) {  
            response.put("mensaje", "Error al realizar la consulta en la base de datos");  
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));  
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
        }  
    }
    

    @PostMapping("/create")  
    public ResponseEntity<?> crear(@Valid @RequestBody BankTransfer bankTransfer, BindingResult result) {  
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

            // Verificar si ya existen registros de BankTransfer para este usuario  
            Optional<BankTransfer> existingBankTransferOpt = bankTransferService.findBankTransferByUser(user);  

            if (existingBankTransferOpt.isPresent()) {  
                // Si existe un registro, actualizar el registro existente  
                BankTransfer existingBankTransfer = existingBankTransferOpt.get();  
                existingBankTransfer.setCuentaBancaria(bankTransfer.getCuentaBancaria());  
                existingBankTransfer.setNombreTitular(bankTransfer.getNombreTitular());  
                existingBankTransfer.setRif(bankTransfer.getRif());  

                // Guardar el registro actualizado  
                BankTransfer updatedBankTransfer = bankTransferService.updateBankTransfer(existingBankTransfer.getId(), existingBankTransfer);  
                response.put("mensaje", "La transferencia bancaria ha sido actualizada con éxito!");  
                response.put("bankTransfer", updatedBankTransfer);  
                return new ResponseEntity<>(response, HttpStatus.OK);  
            } else {  
                // Si no existe un registro, proceder a crear uno nuevo  
                bankTransfer.setUser(user); // Asocia el usuario autenticado a la transferencia  
                BankTransfer createdBankTransfer = bankTransferService.createBankTransfer(bankTransfer);  
                response.put("mensaje", "La transferencia bancaria ha sido creada con éxito!");  
                response.put("bankTransfer", createdBankTransfer);  
                return new ResponseEntity<>(response, HttpStatus.CREATED);  
            }  
        } catch (DataAccessException e) {  
            response.put("mensaje", "Error al realizar la operación en la base de datos");  
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));  
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
        }  
    }

    @GetMapping("/")  
    public ResponseEntity<List<BankTransfer>> getAllBankTransfers() {  
        List<BankTransfer> bankTransfers;  
        try {  
            bankTransfers = bankTransferService.getAllBankTransfers();  
        } catch (DataAccessException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  
        }  

        return ResponseEntity.ok(bankTransfers);  
    }  

    @GetMapping("/{id}")  
    public ResponseEntity<?> getBankTransferById(@PathVariable Long id) {  
        Map<String, Object> response = new HashMap<>();  
        Optional<BankTransfer> bankTransferOpt;  

        try {  
            bankTransferOpt = bankTransferService.getBankTransferById(id);  
        } catch (DataAccessException e) {  
            response.put("mensaje", "Error al realizar la consulta en la base de datos");  
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));  
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
        }  

        if (!bankTransferOpt.isPresent()) {  
            response.put("mensaje", "Error: la transferencia bancaria ID: " + id + " no existe en la base de datos!");  
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);  
        }  

        return ResponseEntity.ok(bankTransferOpt.get());  
    }  

    @PutMapping("/{id}")  
    public ResponseEntity<?> updateBankTransfer(@PathVariable Long id, @Valid @RequestBody BankTransfer bankTransferDetails) {  
        Map<String, Object> response = new HashMap<>();  
        Optional<BankTransfer> existingBankTransferOpt = bankTransferService.getBankTransferById(id);  

        if (!existingBankTransferOpt.isPresent()) {  
            response.put("mensaje", "Error: no se pudo editar, la transferencia bancaria ID: " + id + " no existe en la base de datos!");  
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);  
        }  

        BankTransfer existingBankTransfer = existingBankTransferOpt.get();  

        try {  
            // Actualiza la información de la transferencia bancaria  
            existingBankTransfer.setCuentaBancaria(bankTransferDetails.getCuentaBancaria());  
            existingBankTransfer.setNombreTitular(bankTransferDetails.getNombreTitular());  
            existingBankTransfer.setRif(bankTransferDetails.getRif());  
            existingBankTransfer.setVendorService(bankTransferDetails.getVendorService());  
            existingBankTransfer.setUser(bankTransferDetails.getUser());  
            
            bankTransferService.updateBankTransfer(id, existingBankTransfer);  
        } catch (DataAccessException e) {  
            response.put("mensaje", "Error al actualizar la transferencia bancaria en la base de datos");  
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));  
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
        }  

        response.put("mensaje", "La transferencia bancaria ha sido actualizada con éxito!");  
        response.put("bankTransfer", existingBankTransfer);  
        return new ResponseEntity<>(response, HttpStatus.OK);  
    }  

    @DeleteMapping("/{id}")  
    public ResponseEntity<?> deleteBankTransfer(@PathVariable Long id) {  
        Map<String, Object> response = new HashMap<>();  

        try {  
            Optional<BankTransfer> bankTransferOpt = bankTransferService.getBankTransferById(id);  
            if (!bankTransferOpt.isPresent()) {  
                response.put("mensaje", "Error: la transferencia bancaria ID: " + id + " no existe en la base de datos!");  
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);  
            }  

            bankTransferService.deleteBankTransfer(id);  
        } catch (DataAccessException e) {  
            response.put("mensaje", "Error al eliminar la transferencia bancaria de la base de datos");  
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));  
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
        }  

        response.put("mensaje", "La transferencia bancaria ha sido eliminada con éxito!");  
        return new ResponseEntity<>(response, HttpStatus.OK);  
    }  
}