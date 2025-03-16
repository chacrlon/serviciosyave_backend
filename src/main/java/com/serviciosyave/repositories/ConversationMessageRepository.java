package com.serviciosyave.repositories;

import com.serviciosyave.entities.ConversationMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationMessageRepository extends JpaRepository<ConversationMessage, Long> {
    List<ConversationMessage> findByRoomId(String roomId);
}