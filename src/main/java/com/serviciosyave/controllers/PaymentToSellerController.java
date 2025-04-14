package com.serviciosyave.controllers;

import com.serviciosyave.entities.PaymentToSeller;
import com.serviciosyave.repositories.PaymentToSellerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/admin/paymentseller")
public class PaymentToSellerController {

    @Autowired
    private PaymentToSellerRepository paymentToSellerRepository;

    @GetMapping
    public ResponseEntity<Page<PaymentToSeller>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transactionDate,desc") String sort) {

        Sort.Direction direction = sort.endsWith(",desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = sort.split(",")[0];

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<PaymentToSeller> result = paymentToSellerRepository.findAllWithDetails(pageable);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<Void> approvePayment(@PathVariable Long id) {
        // Implementar lógica de aprobación
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<Void> rejectPayment(@PathVariable Long id) {
        // Implementar lógica de rechazo
        return ResponseEntity.ok().build();
    }
}