package com.serviciosyave.services;

import com.serviciosyave.entities.Ineed;
import com.serviciosyave.entities.VendorService;
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

    public void sendCounterOfferNotification(Negotiate negotiation, Long userId, Ineed ineed, VendorService vendorService) {
        JSONObject json = new JSONObject();
        json.put("type", "counteroffer");
        json.put("amount", negotiation.getAmount());
        json.put("justification", negotiation.getJustification());
        json.put("currentOffer", negotiation.getOfferCount());

        if ("requirement".equals(negotiation.getType())) {
            json.put("ineedId", negotiation.getIneed().getId());
            json.put("typeFlow", "requirement");
        } else if ("service".equals(negotiation.getType())) {
            json.put("ineedId", negotiation.getVendorService().getId());
            json.put("typeFlow", "service");
        }

        json.put("senderId", negotiation.getSender().getId());
        json.put("receiverId", negotiation.getReceiver().getId());
        json.put("negotiateId", negotiation.getId());
        json.put("status", negotiation.getStatus());
        
        if ("requirement".equals(negotiation.getType())) {
            json.put("presupuesto", ineed.getPresupuesto());
            json.put("titulo", ineed.getTitulo());
        } else if ("service".equals(negotiation.getType())) {
            json.put("presupuesto", vendorService.getPrecio());
            json.put("titulo", vendorService.getNombre());
        }

        SseEmitter emitter = emitters.get(userId);
        if(emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("negotiation")
                        .data(json.toString()));
            } catch (IOException e) {
                emitters.remove(userId);
            }
        }
    }

    public void sendNegotiationAcceptedNotification(Negotiate negotiation) {
        JSONObject json = new JSONObject();
        json.put("type", "accepted");
        if ("requirement".equals(negotiation.getType())) {
            json.put("message", "Oferta aceptada para: " + negotiation.getIneed().getTitulo());
            json.put("ineedId", negotiation.getIneed().getId());
            json.put("typeFlow", "requirement");
        } else if ("service".equals(negotiation.getType())) {
            json.put("message", "Oferta aceptada para: " + negotiation.getVendorService().getNombre());
            json.put("ineedId", negotiation.getVendorService().getId());
            json.put("typeFlow", "service");
        }

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