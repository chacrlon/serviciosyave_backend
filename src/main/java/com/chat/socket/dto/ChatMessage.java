package com.chat.socket.dto;  

public class ChatMessage {  
    private String message;  
    private String sender;  
    private String receiver;  

    public ChatMessage() {  
        super();  
    }  

    public ChatMessage(String message, String sender, String receiver) {  
        this.message = message;  
        this.sender = sender;  
        this.receiver = receiver;  
    }  

    public String getMessage() {  
        return message;  
    }  

    public void setMessage(String message) {  
        this.message = message;  
    }  

    public String getSender() {  
        return sender;  
    }  

    public void setSender(String sender) {  
        this.sender = sender;  
    }  

    public String getReceiver() {  
        return receiver;  
    }  

    public void setReceiver(String receiver) {  
        this.receiver = receiver;  
    }  
}