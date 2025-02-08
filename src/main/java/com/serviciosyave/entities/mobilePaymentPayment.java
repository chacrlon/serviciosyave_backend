package com.serviciosyave.entities;  

import jakarta.persistence.*;  

@Entity
@DiscriminatorValue("MOBILE") // Valor para mobilePaymentPayment  
public class mobilePaymentPayment extends Payment {
    private String telefono; // Teléfono con el que se realizó el pago

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	} 
}