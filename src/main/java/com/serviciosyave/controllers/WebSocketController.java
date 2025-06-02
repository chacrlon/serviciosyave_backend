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
import java.util.Optional;

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
        // 1. Log del roomId recibido
        System.out.println("[DEBUG] RoomId recibido: " + roomId);

        Long paymentId = extractPaymentIdFromRoomId(roomId);

        // 2. Log del paymentId extraído
        System.out.println("[DEBUG] PaymentId extraído: " + paymentId);

        ObjectMapper objectMapper = new ObjectMapper();
        ConversationMessage chat = new ConversationMessage();
        chat.setRoomId(roomId);
        chat.setMessage(objectMapper.writeValueAsString(message));
        chat.setTimestamp(LocalDateTime.now());

        ConversationMessage savedConversation = conversationMessageRepository.save(chat);
        Notification notification = null;

        // 3. Log de los IDs de usuario
        System.out.println("[DEBUG] Buscando notificación para userId: " + message.getReceiver()
                + ", userId2: " + message.getSender());

        // Buscar notificación específica por paymentId
        Optional<Notification> lastNotification = notificationRepository
                .findTopByUserIdAndUserId2AndEstatusAndPaymentIdOrderByIdDesc(
                        Long.valueOf(message.getReceiver()),
                        Long.valueOf(message.getSender()),
                        "Message",
                        paymentId  // <-- Incluir paymentId en la búsqueda
                );

        if (lastNotification.isPresent()) {
            notification = lastNotification.get();

            // ACTUALIZAR paymentId además del mensaje
            notification.setPaymentId(paymentId); // <--- AÑADIR ESTA LÍNEA

            // 4. Log de notificación existente
            System.out.println("[DEBUG] Notificación existente encontrada. ID: " + notification.getId()
                    + ", PaymentId actual: " + notification.getPaymentId());

            notificationRepository.updateExistingNotification(
                    notification.getId(),
                    "Tienes un nuevo mensaje: " + message.getMessage(),
                    paymentId
            );
        } else {
            // 5. Log antes de crear nueva notificación
            System.out.println("[DEBUG] Creando NUEVA notificación con paymentId: " + paymentId);

            notification = new Notification(
                    Long.valueOf(message.getReceiver()),
                    "Tienes un nuevo mensaje: " + message.getMessage(),
                    Long.valueOf(message.getSender()),
                    message.getUserType(),
                    message.getVendorServiceId() > 0 ? message.getVendorServiceId() : null,
                    null, // resultadoProveedor
                    null, // resultadoConsumidor
                    "Message",
                    null, // id2
                    message.getIneedId() > 0 ? message.getIneedId() : null,
                    null, // type
                    null, // status
                    null, // amount
                    paymentId  // PaymentId asignado
            );

            notification = notificationRepository.save(notification);

            // 6. Log después de guardar
            System.out.println("[DEBUG] Notificación creada. ID: " + notification.getId()
                    + ", PaymentId guardado: " + notification.getPaymentId());
        }

        // 7. Log antes de enviar SSE
        System.out.println("[DEBUG] Enviando SSE con paymentId: " + paymentId);

        notificationSseController.sendNotification(
                notification.getId(),
                "Tienes un nuevo mensaje: " + message.getMessage(),
                Long.valueOf(message.getReceiver()),
                Long.valueOf(message.getSender()),
                message.getVendorServiceId(),
                message.getIneedId(),
                notification.getUserType(),
                paymentId
        );

        return new ChatMessage(
                message.getMessage(),
                message.getUser(),
                message.getReceiver(),
                message.getSender()
        );
    }

    private Long extractPaymentIdFromRoomId(String roomId) {
        System.out.println("[DEBUG] Extrayendo paymentId de: " + roomId);

        String[] parts = roomId.split("-");
        if (parts.length >= 3) {
            try {
                Long paymentId = Long.parseLong(parts[parts.length - 1]);
                System.out.println("[DEBUG] PaymentId parseado: " + paymentId);
                return paymentId;
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] No se pudo parsear paymentId: " + parts[parts.length - 1]);
                return null;
            }
        }
        System.out.println("[WARN] RoomId no tiene suficientes partes: " + parts.length);
        return null;
    }

    @MessageMapping("/countdown")
    @SendTo("/topic/countdown")
    public CountdownMessage updateCountdown(CountdownMessage message) {
        System.out.println("Actualizando contador para roomId: " + message.getRoomId() + " con valor: " + message.getCountdown());
        return message; // Envía el mensaje a todos los suscriptores
    }
}