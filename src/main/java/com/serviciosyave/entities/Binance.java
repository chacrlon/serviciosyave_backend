package com.serviciosyave.entities;  

import jakarta.persistence.*;  

@Entity   
public class Binance{  
	@Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;  
    private String binanceEmail;  

    @ManyToOne  
    private VendorService vendorService;  

    @ManyToOne  
    private User user;

    
    //Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBinanceEmail() {
		return binanceEmail;
	}

	public void setBinanceEmail(String binanceEmail) {
		this.binanceEmail = binanceEmail;
	}

	public VendorService getVendorService() {
		return vendorService;
	}

	public void setVendorService(VendorService vendorService) {
		this.vendorService = vendorService;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}  
    
    
	    
}