package com.springboot.backend.andres.usersapp.usersbackend.controllers;  

import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  
import org.springframework.messaging.simp.SimpMessagingTemplate;  
import org.springframework.stereotype.Controller;  

@Controller  
public class NotificationController {  

    private final SimpMessagingTemplate messagingTemplate;  
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);  

    public NotificationController(SimpMessagingTemplate messagingTemplate) {  
        this.messagingTemplate = messagingTemplate;  
    }  

    public void notifyUser(Long userId, String notification) {  
        try {  
            logger.info("Notificando al usuario {}: {}", userId, notification);  
            messagingTemplate.convertAndSend("/topic/notifications/" + userId, notification);  
        } catch (Exception e) {  
            logger.error("Error al enviar la notificación al usuario {}: {}", userId, e.getMessage());  
            // Aquí podrías decidir si lanzar una excepción o manejarlo de otra manera  
        }  
    }  
}