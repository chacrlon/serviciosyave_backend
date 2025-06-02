package com.serviciosyave.entities;  

import jakarta.persistence.*;  
import java.util.Date;

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
		private Long ineedId;
	    private String resultadoProveedor;
	    private String resultadoConsumidor;   
	    private String estatus;
	    private Long id2;
		private String type; //requerimiento, servicio
		private String status; //pagado, no_pagado, pendiente
	    private Double amount;

     	@Column(name = "payment_id")
     	private Long paymentId;

		@Column(name = "created_at", updatable = false)
		private Date createdAt = new Date();

	    public Notification() {  
	    }  
	    
	    public Notification(Long userId, String message, Long userId2, String userType,
	                       Long vendorServiceId, String resultadoProveedor, String resultadoConsumidor, String estatus, Long id2, Long ineedId,     String type,  // Nuevo parámetro
							String status, Double amount, Long paymentId) {
	        this.userId = userId;
	        this.message = message;
	        this.isRead = false; // Por defecto, la notificación no está leída
	        this.userId2 = userId2;
	        this.userType = userType;
	        this.vendorServiceId = vendorServiceId;
	        this.resultadoProveedor = resultadoProveedor;
	        this.resultadoConsumidor = resultadoConsumidor;
	        this.estatus = estatus; // Correcto: asignado desde el argumento estatus
	        this.id2 = id2; // Guardar el id de la otra notificación
			this.ineedId=ineedId;
			this.type = type;
			this.status = status;
			this.amount = amount;
			this.paymentId = paymentId;
	    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVendorServiceId() {
		return vendorServiceId;
	}

	public void setVendorServiceId(Long id) {
		this.vendorServiceId = id;
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

	public String getResultadoProveedor() {
		return resultadoProveedor;
	}

	public void setResultadoProveedor(String resultadoProveedor) {
		this.resultadoProveedor = resultadoProveedor;
	}

	public String getResultadoConsumidor() {
		return resultadoConsumidor;
	}

	public void setResultadoConsumidor(String resultadoConsumidor) {
		this.resultadoConsumidor = resultadoConsumidor;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Long getId2() {
		return id2;
	}

	public void setId2(Long id2) {
		this.id2 = id2;
	}

	public Long getIneedId() {
		return ineedId;
	}

	public void setIneedId(Long ineedId) {
		this.ineedId = ineedId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreateAt(Date createdAt) {
		this.createdAt = createdAt;
	}  

}