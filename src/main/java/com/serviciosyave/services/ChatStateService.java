package com.serviciosyave.services;

import com.serviciosyave.entities.ChatState;
import com.serviciosyave.repositories.ChatStateRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ChatStateService {
    private final ChatStateRepository repository;

    public ChatStateService(ChatStateRepository repository) {
        this.repository = repository;
    }

    public ChatState saveState(ChatState state) {
        return repository.save(state);
    }

    public Optional<ChatState> getStateByPaymentId(Long paymentId) {
        return repository.findByPaymentId(paymentId);
    }
}
