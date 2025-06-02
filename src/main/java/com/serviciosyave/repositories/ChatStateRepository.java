package com.serviciosyave.repositories;

import com.serviciosyave.entities.ChatState;
import com.serviciosyave.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatStateRepository extends JpaRepository<ChatState, Long> {
    Optional<ChatState> findByPaymentId(Long paymentId);
}