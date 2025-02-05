package com.serviciosyave.entities;  

import jakarta.persistence.*;  

@Entity   
public class Notification{  
	@Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;  

    private Long userId;
    private String message;
    private boolean isRead;
    private Long userId2;
    private String userType;
    private Long vendorServiceId;
    
    public Notification() {  
    }  
    
    public Notification(Long userId, String message, Long userId2, String userType, Long vendorServiceId) {  
        this.userId = userId;  
        this.message = message;  
        this.isRead = false; // Por defecto, la notificación no está leída  
        this.userId2 = userId2;  
        this.userType = userType;  
        this.vendorServiceId = vendorServiceId;
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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Long getVendorServiceId() {
		return vendorServiceId;
	}

	public void setVendorServiceId(Long vendorServiceId) {
		this.vendorServiceId = vendorServiceId;
	}  
	
	
	    
}