package com.serviciosyave.controllers;

import com.serviciosyave.entities.BinancePayment;
import com.serviciosyave.entities.Payment;
import com.serviciosyave.entities.PaymentDTO;
import com.serviciosyave.entities.bankTransferPayment;
import com.serviciosyave.entities.mobilePaymentPayment;
import com.serviciosyave.services.PaymentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController  
@RequestMapping("/api/payment")  
public class PaymentController {  

    @Autowired  
    private PaymentService paymentService;   

    @PostMapping("/create")  
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentDTO paymentDTO) {  
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  
        Long userId = (Long) authentication.getDetails();
        Long ineedId = paymentDTO.getIneedId();
        Long receiverId = paymentDTO.getReceiverId();

        Payment payment;  

        switch (paymentDTO.getMetodoPago()) {  
        case "pagoMovil":  
            mobilePaymentPayment mobilePayment = new mobilePaymentPayment();  
            mobilePayment.setTelefono(paymentDTO.getTelefono());  
            payment = mobilePayment;  
            payment.setTipoPago("MOBILE"); // Asigna el valor aquí
            break;  
        case "transferenciaBancaria":  
            bankTransferPayment bankTransfer = new bankTransferPayment();  
            bankTransfer.setNumeroCuenta(paymentDTO.getNumeroCuenta());  
            payment = bankTransfer;  
            payment.setTipoPago("BANK_TRANSFER"); // Asigna el valor aquí
            break;  
        case "binance":  
            BinancePayment binancePayment = new BinancePayment();  
            binancePayment.setEmailBinance(paymentDTO.getEmailBinance());  
            payment = binancePayment;  
            payment.setTipoPago("BINANCE"); // Asigna el valor aquí
            break;  
        default:  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();  
    }

        // Asignar campos comunes  
        payment.setMonto(paymentDTO.getMonto());  
        payment.setDivisa(paymentDTO.getDivisa());  
        payment.setMetodoPago(paymentDTO.getMetodoPago());  
        payment.setReferencia(paymentDTO.getReferencia());  
        payment.setEstatus("procesando");  
        payment.setVendorServiceId(paymentDTO.getVendorServiceId());  
        payment.setUsersId(userId);
        payment.setIneedId(ineedId);
        payment.setReceiverId(receiverId);

        // Guardar el pago en la base de datos  
        Payment createdPayment = paymentService.createdPayment(payment);  
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPayment);  
    }

    @GetMapping("/all")  
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        try {
            List<PaymentDTO> payments = paymentService.getAllPayments();
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/approve/{id}")  
    public ResponseEntity<Void> approvePayment(@PathVariable Long id) {  
        try {  
            paymentService.approvePayment(id);  
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  
        } catch (RuntimeException e) {  
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // O manejar otros errores según sea necesario  
        }  
    }

    @PutMapping("/reject/{id}")  
    public ResponseEntity<Void> rejectPayment(@PathVariable Long id) {  
        paymentService.rejectPayment(id);  
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  
    }  

    
}