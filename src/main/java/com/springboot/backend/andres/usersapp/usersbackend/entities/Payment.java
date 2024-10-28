package com.springboot.backend.andres.usersapp.usersbackend.entities;  

import jakarta.persistence.*;  

@Entity
public class Payment {  
	@Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;  
	private Double monto;
	private String divisa; //bolivares, usdt para binance y usd para paypal
	private String metodo_pago; //Identificar si fue pago movil, binance o transferencia
	private String referencia; 
	private String estatus; //procesando, aprobado y rechazado

	@Column(name = "vendor_service_id") // Guardamos solo el ID del servicio 
    private Long vendorServiceId;  

    @Column(name = "users_id") 
    private Long userId; // Guardamos solo el ID del usuario  

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

	public String getDivisa() {
		return divisa;
	}

	public void setDivisa(String divisa) {
		this.divisa = divisa;
	}

	public String getMetodo_pago() {
		return metodo_pago;
	}

	public void setMetodo_pago(String metodo_pago) {
		this.metodo_pago = metodo_pago;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Long getVendorServiceId() {
		return vendorServiceId;
	}

	public void setVendorServiceId(Long vendorServiceId) {
		this.vendorServiceId = vendorServiceId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	
	
    
    
	
}