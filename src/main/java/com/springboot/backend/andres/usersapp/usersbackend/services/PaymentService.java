package com.springboot.backend.andres.usersapp.usersbackend.services;  

import org.springframework.stereotype.Service;  
import org.springframework.beans.factory.annotation.Autowired;

import com.springboot.backend.andres.usersapp.usersbackend.controllers.NotificationController;
import com.springboot.backend.andres.usersapp.usersbackend.entities.Payment;  
import com.springboot.backend.andres.usersapp.usersbackend.entities.PaymentDTO;  
import com.springboot.backend.andres.usersapp.usersbackend.entities.User;  
import com.springboot.backend.andres.usersapp.usersbackend.entities.VendorService; // Asegúrate de importar la entidad correcta  
import com.springboot.backend.andres.usersapp.usersbackend.repositories.PaymentRepository;  
import com.springboot.backend.andres.usersapp.usersbackend.repositories.UserRepository;  
import com.springboot.backend.andres.usersapp.usersbackend.repositories.VendorServiceRepository; // Importa el repositorio de VendorService  
import java.util.ArrayList;  
import java.util.List;  

@Service  
public class PaymentService {  

    @Autowired  
    private PaymentRepository paymentRepository;  

    @Autowired  
    private UserRepository userRepository;  

    @Autowired  
    private VendorServiceRepository vendorServiceRepository; // Añadir el repositorio de VendorService  

    @Autowired  
    private EmailService emailService;  

    @Autowired  
    private NotificationController notificationController;  

    // Método para guardar la ubicación  
    public Payment createdPayment(Payment payment) {  
        return paymentRepository.save(payment);  
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
    
    public void approvePayment(Long paymentId) {  
        Payment payment = paymentRepository.findById(paymentId)  
            .orElseThrow(() -> new RuntimeException("Pago no encontrado"));  

        // Cambiar el estado del pago a aprobado  
        payment.setEstatus("aprobado");  
        paymentRepository.save(payment);  

        // Obtener el servicio del vendedor asociado al pago  
        Long vendorServiceId = payment.getVendorServiceId();  
        VendorService vendorService = vendorServiceRepository.findById(vendorServiceId)  
            .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));  

        // Obtener el ID del usuario asociado al servicio  
        Long userId = vendorService.getUserId();  
        String userEmail = userRepository.findById(userId)  
            .map(User::getEmail) // Asumiendo que tienes un método getEmail en la clase User  
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));  

        // Mensaje de notificación  
        String message = "El usuario " + payment.getUserId() + " ha solicitado tu servicio " + vendorService.getNombre() + ".";  
        
        // Enviar correo electrónico  
       // emailService.sendEmail(userEmail, "Notificación de Servicio", message);  
        
        // Enviar notificación en tiempo real  
        notificationController.notifyUser(userId, message);  
    }  

    public void rejectPayment(Long paymentId) {  
        Payment payment = paymentRepository.findById(paymentId).orElse(null);  
        if (payment != null) {  
            payment.setEstatus("rechazado");  
            paymentRepository.save(payment);  
        }  
    }  
}