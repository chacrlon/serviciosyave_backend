package com.serviciosyave.controllers;  

import org.springframework.http.MediaType;  
import org.springframework.web.bind.annotation.GetMapping;  
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;  
import java.io.PrintWriter;  
import java.util.concurrent.ConcurrentLinkedQueue;  

@RestController  
public class NotificationSseController {  

    private final ConcurrentLinkedQueue<PrintWriter> clients = new ConcurrentLinkedQueue<>();  

    @GetMapping(value = "/notifications", produces = MediaType.TEXT_EVENT_STREAM_VALUE)  
    public void streamNotifications(HttpServletResponse response) throws IOException {  
        response.setContentType("text/event-stream");  
        response.setCharacterEncoding("UTF-8");  

        PrintWriter writer = response.getWriter();  
        clients.add(writer);  

        // Mantener la conexión abierta  
        while (true) {  
            try {  
                Thread.sleep(10000); // Mantener la conexión abierta  
            } catch (InterruptedException e) {  
                break;  
            }  
        }  
    }  

    public void sendNotification(Long id, String message, Long userId, Long userId2) {  
        // Crear un objeto Notification para enviar   
        String jsonMessage = String.format("{\"id\": %d, \"userId\": %d, \"message\": \"%s\", \"read\": false, \"userId2\": %d}", id, userId, message, userId2);  
        System.out.println(jsonMessage);  
        for (PrintWriter client : clients) {  
            client.write("data: " + jsonMessage + "\n\n");  
            client.flush();  
        }  
    } 
}