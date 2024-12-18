package com.springboot.backend.andres.usersapp.usersbackend.controllers;  

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.web.bind.annotation.*;  
import com.springboot.backend.andres.usersapp.usersbackend.entities.Notification;  
import com.springboot.backend.andres.usersapp.usersbackend.repositories.NotificationRepository;  
import java.util.List;  

@RestController  
@RequestMapping("/api/notifications")  
public class NotificationController {  
    @Autowired  
    private NotificationRepository notificationRepository;   
    
    @Autowired  
    private NotificationSseController notificationSseController;   

    public void notifyUser(Long sellerId, Long buyerId, String message) {  
        List<Notification> existingNotifications = notificationRepository.findByUserIdAndMessageAndIsRead(sellerId, message, false);  
        if (existingNotifications.isEmpty()) {  
            Notification notification = new Notification(sellerId, message, buyerId); // Incluye userId2  
            notification = notificationRepository.save(notification); // Guarda la notificación  

            // Enviar notificación en tiempo real con el ID  
            notificationSseController.sendNotification(notification.getId(), message, sellerId, buyerId); // Asegúrate de pasar buyerId aquí  
        }  
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