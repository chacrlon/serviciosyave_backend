package com.serviciosyave.dto;  

public class ChatMessage {  
    private String message;  
    private String sender;  
    private String receiver;  
    private String user;
	private Long vendorServiceId;
	private Long ineedId;

	public ChatMessage() {
        super();  
    }
    
	public ChatMessage(String message, String sender, String receiver, String user) {
		super();
		this.message = message;
		this.sender = sender;
		this.receiver = receiver;
		this.user = user;
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
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Long getIneedId() {
		return ineedId;
	}

	public void setIneedId(Long ineedId) {
		this.ineedId = ineedId;
	}

	public Long getVendorServiceId() {
		return vendorServiceId;
	}

	public void setVendorServiceId(Long vendorServiceId) {
		this.vendorServiceId = vendorServiceId;
	}
    
}