package com.serviciosyave.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.serviciosyave.entities.Notification;
import com.serviciosyave.entities.UserStatus;
import com.serviciosyave.repositories.NotificationRepository;
import com.serviciosyave.services.NotificationService;
import com.serviciosyave.services.PaymentToSellerService;
import com.serviciosyave.services.UserService;

import java.util.List;

@RestController  
@RequestMapping("/api/notifications")  
public class NotificationController {  
	
    @Autowired  
    private NotificationRepository notificationRepository;   
    
    @Autowired  
    private NotificationSseController notificationSseController; 
    
    @Autowired  
    private NotificationService notificationService;
    
    @Autowired  
    private UserService userService;
    
    @Autowired
    private PaymentToSellerService paymentToSellerService;

    public Long notifyUser(Long sellerId, Long buyerId, String message, String userType, Long vendorServiceId, Long ineedId, String type,   // Nuevo parámetro
                           String status, Double amount, Long paymentId) {
        List<Notification> existingNotifications = notificationRepository.findByUserIdAndMessageAndIsRead(sellerId, message, false);
        if (existingNotifications.isEmpty()) {
            Notification notification = new Notification(
                sellerId,
                message,
                buyerId,
                userType,
                vendorServiceId,
                null,
                null,
                userType,
                null,
                ineedId,
                    type,
                    status,
                    amount,
                    paymentId
            );
            notification = notificationRepository.save(notification);

            // Enviar notificación en tiempo real con el ID
            notificationSseController.sendNotification(notification.getId(), message, sellerId, buyerId, vendorServiceId, ineedId, userType, paymentId  );

            return notification.getId(); // Devuelve el ID de la notificación creada
        }
        return null; // Retorna nulo si ya existe la notificación
    }
    
    @PutMapping("/approve/provider/{id}/{id2}")  
    public ResponseEntity<List<Notification>> approveServiceByProvider(@PathVariable Long id, @PathVariable Long id2) {  
        List<Notification> updatedNotifications = notificationService.approveServiceByProvider(id, id2);  
        return ResponseEntity.ok(updatedNotifications);  
    }  
    

    @PutMapping("/approve/client/{id}/{id2}")
    public ResponseEntity<List<Notification>> approveServiceByClient(
            @PathVariable Long id, 
            @PathVariable Long id2) {
        
        List<Notification> updatedNotifications = notificationService.approveServiceByClient(id, id2);
        
        // Procesar pago
        paymentToSellerService.processPayment(id); 
        
        // Cambiar estado del usuario
        userService.updateUserStatus(id, UserStatus.NO_OCUPADO);
        
        return ResponseEntity.ok(updatedNotifications);
    } 

    @PutMapping("/reject/provider/{id}/{id2}")  
    public ResponseEntity<List<Notification>> rejectServiceByProvider(@PathVariable Long id, @PathVariable Long id2) {  
        List<Notification> updatedNotifications = notificationService.rejectServiceByProvider(id, id2);  
        // Cambiar el estado del vendedor a NO_OCUPADO  
        userService.updateUserStatus(id, UserStatus.NO_OCUPADO);  
        return ResponseEntity.ok(updatedNotifications);  
    }  

    @PutMapping("/reject/client/{id}/{id2}")  
    public ResponseEntity<List<Notification>> rejectServiceByClient(@PathVariable Long id, @PathVariable Long id2) {  
        List<Notification> updatedNotifications = notificationService.rejectServiceByClient(id, id2);  
        // Cambiar el estado del comprador a NO_OCUPADO  
        userService.updateUserStatus(id, UserStatus.NO_OCUPADO);  
        return ResponseEntity.ok(updatedNotifications);  
    }

    
    
    // Obtener notificaciones de un usuario  
    @GetMapping("/{userId}")  
    public List<Notification> getUserNotifications(@PathVariable Long userId) {  
        return notificationRepository.findByUserId(userId);  
    }  

    // Marcar una notificación como leída  
    @PutMapping("/read/{id}")  
    public void markAsRead(@PathVariable Long id) {  
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Notificación no encontrada"));  
        notification.setRead(true);  
        notificationRepository.save(notification);  
    }  
}