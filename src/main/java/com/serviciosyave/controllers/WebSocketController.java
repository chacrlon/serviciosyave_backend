package com.serviciosyave.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.serviciosyave.dto.ChatMessage;
import com.serviciosyave.entities.CountdownMessage;

@Controller
public class WebSocketController {
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage chat(@DestinationVariable String roomId, ChatMessage message) {
        System.out.println(message);
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