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

    // Crear una nueva notificación  
    public void notifyUser(Long userId, String message) {  
        Notification notification = new Notification(userId, message);    
        
        // Enviar notificación en tiempo real  
        notificationSseController.sendNotification(message);  
        notificationRepository.save(notification);  
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



/*
package com.springboot.backend.andres.usersapp.usersbackend.controllers;  

import org.springframework.messaging.handler.annotation.DestinationVariable;  
import org.springframework.messaging.handler.annotation.MessageMapping;  
import org.springframework.messaging.handler.annotation.SendTo;  
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.chat.socket.dto.ChatMessage; 
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.messaging.simp.SimpMessagingTemplate;

@CrossOrigin(origins = "http://localhost:4200")
@Controller  
public class NotificationController { 
	@Autowired  
    private SimpMessagingTemplate messagingTemplate; 

    @MessageMapping("/chat/{roomId}")  
    @SendTo("/topic/{roomId}")  
    public ChatMessage chat(@DestinationVariable String roomId, ChatMessage message) {  
        System.out.println("Enviando mensaje: " + message.getMessage());  
        return message;  
    }  

    public void notifyUser(Long userId, String message) {  
        // Lógica para enviar el mensaje al usuario específico  
        // Esto podría involucrar enviar al destino /topic/user-{userId}  
    	 
        ChatMessage chatMessage = new ChatMessage(message, "System", "User-" + userId); 
        System.out.println("Enviando mensaje: " + chatMessage.getMessage()); 
        // Aquí se debería implementar la lógica para enviar el mensaje al canal adecuado
        // Enviar el mensaje al destino específico  
        messagingTemplate.convertAndSend("/topic/user-" + userId, chatMessage); 
    }  
}
*/