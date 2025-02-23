package com.serviciosyave.services;

import com.serviciosyave.entities.Negotiate;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseService {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void addEmitter(Long userId, SseEmitter emitter) {
        emitters.put(userId, emitter);
        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
    }

    public void sendCounterOfferNotification(Negotiate negotiation) {
        JSONObject json = new JSONObject();
        json.put("type", "counteroffer");
        json.put("amount", negotiation.getAmount());
        json.put("justification", negotiation.getJustification());
        json.put("currentOffer", negotiation.getOfferCount());
        json.put("ineedId", negotiation.getIneed().getId());
        json.put("senderId", negotiation.getSender().getId());

        SseEmitter emitter = emitters.get(negotiation.getReceiver().getId());
        if(emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("negotiation")
                        .data(json.toString()));
            } catch (IOException e) {
                emitters.remove(negotiation.getReceiver().getId());
            }
        }
    }

    public void sendNegotiationAcceptedNotification(Negotiate negotiation) {
        JSONObject json = new JSONObject();
        json.put("type", "accepted");
        json.put("message", "Oferta aceptada para: " + negotiation.getIneed().getTitulo());
        json.put("ineedId", negotiation.getIneed().getId());

        notifyUser(negotiation.getSender().getId(), json.toString());
        notifyUser(negotiation.getReceiver().getId(), json.toString());
    }

    private void notifyUser(Long userId, String message) {
        SseEmitter emitter = emitters.get(userId);
        if(emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("negotiation")
                        .data(message));
            } catch (IOException e) {
                emitters.remove(userId);
            }
        }
    }
}