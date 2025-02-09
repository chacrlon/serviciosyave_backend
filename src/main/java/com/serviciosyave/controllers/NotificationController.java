package com.serviciosyave.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.serviciosyave.entities.Notification;
import com.serviciosyave.repositories.NotificationRepository;
import com.serviciosyave.services.NotificationService;

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

    public void notifyUser(Long sellerId, Long buyerId, String message, String userType, Long vendorServiceId) {  
        List<Notification> existingNotifications = notificationRepository.findByUserIdAndMessageAndIsRead(sellerId, message, false);  
        if (existingNotifications.isEmpty()) {  
            Notification notification = new Notification(sellerId, message, buyerId, userType, vendorServiceId, userType, userType, userType);  
            notification = notificationRepository.save(notification); // Guarda la notificación  

            // Enviar notificación en tiempo real con el ID  
            notificationSseController.sendNotification(notification.getId(), message, sellerId, buyerId); // Asegúrate de pasar buyerId aquí  
        }  
    }
    
    
    @PutMapping("/approve/provider/{id}")  
    public ResponseEntity<Notification> approveServiceByProvider(@PathVariable Long id) {  
        Notification updatedNotification = notificationService.approveServiceByProvider(id);  
        return ResponseEntity.ok(updatedNotification);  
    }  

    @PutMapping("/approve/client/{id}")  
    public ResponseEntity<Notification> approveServiceByClient(@PathVariable Long id) {  
        Notification updatedNotification = notificationService.approveServiceByClient(id);  
        return ResponseEntity.ok(updatedNotification);  
    }  
    
 // Endpoint para rechazar el servicio por el proveedor  
    @PutMapping("/reject/provider/{id}")  
    public ResponseEntity<Notification> rejectServiceByProvider(@PathVariable Long id) {  
        Notification updatedNotification = notificationService.rejectServiceByProvider(id);  
        return ResponseEntity.ok(updatedNotification);  
    }  

    // Endpoint para rechazar el servicio por el cliente  
    @PutMapping("/reject/client/{id}")  
    public ResponseEntity<Notification> rejectServiceByClient(@PathVariable Long id) {  
        Notification updatedNotification = notificationService.rejectServiceByClient(id);  
        return ResponseEntity.ok(updatedNotification);  
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