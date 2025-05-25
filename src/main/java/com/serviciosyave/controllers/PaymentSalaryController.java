package com.serviciosyave.controllers;

import com.serviciosyave.Enum.PaymentUpdateRequest;
import com.serviciosyave.entities.PaymentSalary;
import com.serviciosyave.services.PaymentSalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PaymentSalaryController {

    @Autowired
    private PaymentSalaryService paymentSalaryService;

    // Obtener pagos pendientes
    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending")
    public ResponseEntity<List<PaymentSalary>> getPendingPayments() {
        return ResponseEntity.ok(paymentSalaryService.getPendingPayments());
    }

    @PostMapping("/generate")
    public ResponseEntity<Void> generatePayments() {
        paymentSalaryService.generatePendingPayments();
        return ResponseEntity.ok().build();
    }

    // Actualizar pago individual
    @PutMapping("/{paymentId}")
    public ResponseEntity<PaymentSalary> updatePayment(
            @PathVariable Long paymentId,
            @RequestBody PaymentUpdateRequest request
    ) {
        return ResponseEntity.ok(paymentSalaryService.updatePayment(paymentId, request));
    }
}
