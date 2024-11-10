package com.springboot.backend.andres.usersapp.usersbackend.entities;  

import jakarta.persistence.*;  

@Entity   
public class Notification{  
	@Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;  

    private Long userId; // ID del usuario que recibe la notificación  
    private String message; // Mensaje de la notificación  
    private boolean isRead; // Estado de lectura de la notificación  

    // Constructor, getters y setters  
    public Notification() {  
    }
    
 // Constructor que acepta userId y message  
    public Notification(Long userId, String message) {  
        this.userId = userId;  
        this.message = message;  
        this.isRead = false; // Por defecto, la notificación no está leída  
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
	    
}