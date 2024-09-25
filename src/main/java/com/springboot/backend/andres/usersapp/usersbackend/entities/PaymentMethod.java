package com.springboot.backend.andres.usersapp.usersbackend.entities;  

import jakarta.persistence.*;  

@Entity  
@Table(name = "payment_method") // Nombre de la tabla en la base de datos  
public class PaymentMethod {  
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;  // Identificador único para cada método de pago  
    private Double monto;  
    private String referencia;  
    private String estado; // "procesando", "pagado"    
    
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
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

    
	    
}