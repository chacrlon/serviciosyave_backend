package com.springboot.backend.andres.usersapp.usersbackend.entities;  

import jakarta.persistence.*;  

@Entity
public class PaymentTransaction {  
	@Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;  
    private Double monto;  
    private String referencia;  

    @ManyToOne  
    private VendorService vendorService;  

    @ManyToOne  
    private User user;  

    @ManyToOne  
    private PaymentMethod paymentMethod; // Puede ser Binance, MobilePayment o BankTransfer  

    
    // Getters y Setters  
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
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

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
    
	    
}