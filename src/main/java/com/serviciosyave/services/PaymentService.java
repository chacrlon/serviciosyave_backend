package com.serviciosyave.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.serviciosyave.controllers.NotificationController;
import com.serviciosyave.entities.Payment;
import com.serviciosyave.entities.PaymentDTO;
import com.serviciosyave.entities.User;
import com.serviciosyave.entities.VendorService;
import com.serviciosyave.repositories.PaymentRepository;
import com.serviciosyave.repositories.UserRepository;
import com.serviciosyave.repositories.VendorServiceRepository;
import java.util.ArrayList;  
import java.util.List;

@Service  
public class PaymentService {  

    @Autowired  
    private PaymentRepository paymentRepository;  

    @Autowired  
    private UserRepository userRepository;  

    @Autowired  
    private VendorServiceRepository vendorServiceRepository;  

    @Autowired  
    private EmailService emailService;  

    @Autowired  
    private NotificationController notificationController;  

    public Payment createdPayment(Payment payment) {  
        return paymentRepository.save(payment);  
    }  
    
    public void approvePayment(Long paymentId) {  
        Payment payment = paymentRepository.findById(paymentId)  
            .orElseThrow(() -> new RuntimeException("Pago no encontrado"));  

        payment.setEstatus("aprobado");  
        paymentRepository.save(payment);  

        Long vendorServiceId = payment.getVendorServiceId();  
        VendorService vendorService = vendorServiceRepository.findById(vendorServiceId)  
            .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));  

        Long sellerId = vendorService.getUserId();  
        User seller = userRepository.findById(sellerId)  
            .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));  

        User buyer = userRepository.findById(payment.getUserId())  
            .orElseThrow(() -> new RuntimeException("Comprador no encontrado"));  

        String messageToSeller = "El usuario " + buyer.getUsername() + " ha comprado tu servicio " + vendorService.getNombre() + ".";  
        String messageToBuyer = "Has comprado el servicio " + vendorService.getNombre() + " de " + seller.getUsername() + ".";  

        emailService.sendEmail(seller.getEmail(), "Notificación de Servicio", messageToSeller);  
        emailService.sendEmail(buyer.getEmail(), "Confirmación de Compra", messageToBuyer);  

     // Crear notificaciones en lugar de enviar mensajes  
        notificationController.notifyUser(seller.getId(), buyer.getId(), messageToSeller, vendorService.getId());
        notificationController.notifyUser(buyer.getId(), seller.getId(), messageToBuyer, vendorService.getId());
    }  

    public List<PaymentDTO> getAllPayments() {  
        List<Payment> payments = paymentRepository.findAll();  
        List<PaymentDTO> paymentDTOs = new ArrayList<>();  
        
        for (Payment payment : payments) {  
            User user = userRepository.findById(payment.getUserId()).orElse(null);  
            PaymentDTO paymentDTO = new PaymentDTO();  
            paymentDTO.setId(payment.getId());  
            paymentDTO.setMonto(payment.getMonto());  
            paymentDTO.setDivisa(payment.getDivisa());  
            paymentDTO.setMetodo_pago(payment.getMetodo_pago());  
            paymentDTO.setReferencia(payment.getReferencia());  
            paymentDTO.setEstatus(payment.getEstatus());  
            paymentDTO.setUsername(user != null ? user.getUsername() : "Desconocido");  
            paymentDTOs.add(paymentDTO);  
        }  
        
        return paymentDTOs;  
    }  
    
    

    public void rejectPayment(Long paymentId) {  
        Payment payment = paymentRepository.findById(paymentId).orElse(null);  
        if (payment != null) {  
            payment.setEstatus("rechazado");  
            paymentRepository.save(payment);  
        }  
    }  
}