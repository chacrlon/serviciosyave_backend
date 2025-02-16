package com.serviciosyave.entities;  

import jakarta.persistence.*;  

@Entity  
public class MobilePayment{  
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id; 

    private String cedula; 
    private String numeroTelefono; 
    private String banco;

    @ManyToOne  
    private VendorService vendorService; // Relación con el servicio del vendedor  

    @ManyToOne  
    private User user; // Relación con el usuario  

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
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