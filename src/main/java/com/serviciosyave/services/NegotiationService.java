package com.serviciosyave.services;

import com.serviciosyave.Enum.NegotiationStatus;
import com.serviciosyave.dto.NegotiationDTO;
import com.serviciosyave.entities.Ineed;
import com.serviciosyave.entities.Negotiate;
import com.serviciosyave.entities.User;
import com.serviciosyave.repositories.NegotiateRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class NegotiationService {
    static final int MAX_OFFERS = 3;

    @Autowired
    private NegotiateRepository negotiateRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SseService sseService;

    @Autowired
    private IneedService ineedService;


    public Negotiate createNegotiation(NegotiationDTO dto) {
        User sender = userService.getUserById(dto.getSenderUserId());
        User receiver = userService.getUserById(dto.getReceiverUserId());
        Ineed ineed = ineedService.getIneedById(dto.getIneedId());
        Long negotiationId = dto.getId();
        NegotiationStatus negotiationStatus = dto.getNegotiationStatus();
        Negotiate negotiation = new Negotiate();
        Long sendId = dto.getSendId();
        int currentCount = 0;

        if(negotiationId != null) {
            negotiation.setId(negotiationId);
            Optional<Negotiate> negotiateRepo = negotiateRepository.findById(negotiationId);
            currentCount=negotiateRepo.get().getOfferCount();
        } else {
            String threadId = generateThreadId(ineed.getId(), sender.getId(), receiver.getId());
            currentCount = negotiateRepository.countByThreadId(threadId);
        }

        if(currentCount >= MAX_OFFERS) {
            throw new IllegalStateException("Límite de ofertas alcanzado");
        }

        negotiation.setAmount(dto.getAmount());
        negotiation.setJustification(dto.getJustification());
        negotiation.setIneed(ineed);
        negotiation.setSender(sender);
        negotiation.setReceiver(receiver);
        negotiation.setOfferCount(currentCount + 1);
        negotiation.setStatus(negotiationStatus);

        Negotiate saved = negotiateRepository.save(negotiation);

        // Notificar via SSE
        sseService.sendCounterOfferNotification(saved, sendId, ineed);

        return saved;
    }

    public Negotiate update(NegotiationDTO dto) {
        User sender = userService.getUserById(dto.getSenderUserId());
        User receiver = userService.getUserById(dto.getReceiverUserId());
        Ineed ineed = ineedService.getIneedById(dto.getIneedId());
        Long negotiationId = dto.getId();
        String justify = dto.getJustification();
        Double amount = dto.getAmount();
        NegotiationStatus negotiationStatus = dto.getNegotiationStatus();
        Long sendId = dto.getSendId();
        Negotiate negotiation = new Negotiate();

        Optional<Negotiate> negotiateRepo = negotiateRepository.findById(negotiationId);
        int newCurrentOffer = negotiateRepo.get().getOfferCount();
        if(
            negotiationStatus != NegotiationStatus.ACCEPTED &&
            negotiationStatus != NegotiationStatus.REJECTED
        ) {
            newCurrentOffer = negotiateRepo.get().getOfferCount()+ 1;
        }

        negotiation.setId(negotiationId);
        negotiation.setAmount(amount);
        negotiation.setJustification(justify);
        negotiation.setIneed(ineed);
        negotiation.setSender(sender);
        negotiation.setReceiver(receiver);
        negotiation.setOfferCount(newCurrentOffer);
        negotiation.setStatus(negotiationStatus);

        Negotiate saved = negotiateRepository.save(negotiation);

        // Notificar via SSE
        sseService.sendCounterOfferNotification(saved, sendId, ineed);

        return saved;
    }

    private String generateThreadId(Long ineedId, Long senderId, Long receiverId) {
        return String.format("%d-%d-%d",
                ineedId,
                Math.min(senderId, receiverId),
                Math.max(senderId, receiverId));
    }

    // En NegotiationService
    public void acceptNegotiation(Long negotiationId) {
        Negotiate negotiation = negotiateRepository.findById(negotiationId)
                .orElseThrow(() -> new EntityNotFoundException("Negociación no encontrada con ID: " + negotiationId));

        // Validar que la negociación está activa
        if(negotiation.getStatus() != NegotiationStatus.ACTIVE) {
            throw new IllegalStateException("La negociación no está activa");
        }

        // Actualizar estado
        negotiation.setStatus(NegotiationStatus.ACCEPTED);
        negotiateRepository.save(negotiation);

        // Notificar a ambas partes
        sseService.sendNegotiationAcceptedNotification(negotiation);
    }

    public Negotiate getNegotiation(NegotiationDTO request) {
        Negotiate negotiation = negotiateRepository.findByIneedIdAndReceiverIdAndSenderId(request.getIneedId(), request.getReceiverUserId(), request.getSenderUserId())
                .orElseThrow(() -> new EntityNotFoundException("Servicio no posee negociación."));
        return negotiation;
    }
}