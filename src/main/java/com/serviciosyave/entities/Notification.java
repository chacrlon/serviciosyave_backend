package com.serviciosyave.entities;  

import jakarta.persistence.*;  

@Entity   
public class Notification{  
	@Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;  

    private Long userId; // ID del usuario que recibe la notificación 
    private String message; // Mensaje de la notificación  
    private boolean isRead; // Estado de lectura de la notificación  
    private Long userId2; // ID del otro usuario que interactua contigo
    
    // Constructor, getters y setters  
    public Notification() {  
    }
    
 // Constructor que acepta userId, message y userId2  
    public Notification(Long userId, String message, Long userId2) {  
        this.userId = userId;  
        this.message = message;  
        this.isRead = false; // Por defecto, la notificación no está leída  
        this.userId2 = userId2; // Establece el ID del comprador  
    }  


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public Long getUserId2() {
		return userId2;
	}

	public void setUserId2(Long userId2) {
		this.userId2 = userId2;
	}  
	
	
	    
}