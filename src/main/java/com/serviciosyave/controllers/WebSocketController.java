package com.serviciosyave.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.serviciosyave.entities.ConversationMessage;
import com.serviciosyave.entities.Notification;
import com.serviciosyave.repositories.ConversationMessageRepository;
import com.serviciosyave.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.serviciosyave.dto.ChatMessage;
import com.serviciosyave.entities.CountdownMessage;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class WebSocketController {

    @Autowired
    private ConversationMessageRepository conversationMessageRepository;

    @Autowired
    private NotificationSseController notificationSseController;

    @Autowired
    private NotificationRepository notificationRepository;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage chat(@DestinationVariable String roomId, ChatMessage message) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ConversationMessage chat = new ConversationMessage();
                            chat.setRoomId(roomId);
                            chat.setMessage(objectMapper.writeValueAsString(message));
                            chat.setTimestamp(LocalDateTime.now());

        ConversationMessage savedConversation = conversationMessageRepository.save(chat);

        Notification notification = new Notification(
                Long.valueOf(message.getReceiver()),
                "Tienes un nuevo mensaje: "+message.getMessage(),
                Long.valueOf(message.getUser()),
                "Message",
                message.getVendorServiceId() > 0 ? message.getVendorServiceId() : null,
                null,
                null,
                null,
                null,
                message.getIneedId() > 0 ? message.getIneedId() : null
        );
        notification = notificationRepository.save(notification);

        notificationSseController.sendNotification(notification.getId(),"Tienes un nuevo mensaje: "+message.getMessage(), Long.valueOf(message.getUser()), Long.valueOf(message.getSender()), message.getVendorServiceId(), message.getIneedId(), notification.getUserType());

        return new ChatMessage(
        		message.getMessage(), 
        		message.getUser(),
        		message.getReceiver(),
        		message.getSender());
    }

    @MessageMapping("/countdown")
    @SendTo("/topic/countdown")
    public CountdownMessage updateCountdown(CountdownMessage message) {
        System.out.println("Actualizando contador para roomId: " + message.getRoomId() + " con valor: " + message.getCountdown());
        return message; // Envía el mensaje a todos los suscriptores
    }
}