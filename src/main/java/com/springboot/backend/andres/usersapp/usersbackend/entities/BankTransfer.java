package com.springboot.backend.andres.usersapp.usersbackend.entities;  

import jakarta.persistence.*;  

@Entity  
public class BankTransfer{  
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id; // Identificador único  

    private String cuentaBancaria; // Número de cuenta bancaria  
    private String nombreTitular; // Nombre del titular de la cuenta  
    private String rif; // RIF del titular  

    // No es necesario incluir monto, referencia y estado aquí, ya que se manejarán en PaymentTransaction  

    @ManyToOne  
    private VendorService vendorService; // Relación con el servicio del vendedor  

    @ManyToOne  
    private User user; // Relación con el usuario  

    
 // Getters y Setters 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCuentaBancaria() {
		return cuentaBancaria;
	}

	public void setCuentaBancaria(String cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}

	public String getNombreTitular() {
		return nombreTitular;
	}

	public void setNombreTitular(String nombreTitular) {
		this.nombreTitular = nombreTitular;
	}

	public String getRif() {
		return rif;
	}

	public void setRif(String rif) {
		this.rif = rif;
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