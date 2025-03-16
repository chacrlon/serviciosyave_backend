package com.serviciosyave.controllers;

import com.serviciosyave.entities.ConversationMessage;
import com.serviciosyave.repositories.ConversationMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ConversationMessageRepository conversationMessageRepository;

    @GetMapping("/{roomId}")
    public List<ConversationMessage> getChatHistory(@PathVariable String roomId) {
        return conversationMessageRepository.findByRoomId(roomId);
    }
}