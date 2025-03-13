package com.serviciosyave.entities;  

public class PaymentDTO {  
    private Long id;
    private Double monto;
    private String divisa;
    private String metodoPago;
    private String referencia;
    private String estatus;
    private String username; 
    private Long vendorServiceId;
    private String telefono;
    private String numeroCuenta;
    private String emailBinance;
	private Long ineedId;
	private Long receiverId;

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

	  
	public String getReferencia() {  
		return referencia;  
	}  
	  
	public void setReferencia(String referencia) {  
		this.referencia = referencia;  
	}  
	  
	public String getEstatus() {  
		return estatus;  
	}  
	  
	public void setEstatus(String estatus) {  
		this.estatus = estatus;  
	}  

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getEmailBinance() {
		return emailBinance;
	}

	public void setEmailBinance(String emailBinance) {
		this.emailBinance = emailBinance;
	}

	public String getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}

	public Long getVendorServiceId() {
		return vendorServiceId;
	}

	public void setVendorServiceId(Long vendorServiceId) {
		this.vendorServiceId = vendorServiceId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getIneedId() {
		return ineedId;
	}

	public void setIneedId(Long ineedId) {
		this.ineedId = ineedId;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

}