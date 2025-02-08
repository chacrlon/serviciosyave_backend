package com.serviciosyave.entities;  

import jakarta.persistence.*;  

@Entity
@DiscriminatorValue("BINANCE")
public class BinancePayment extends Payment {
    private String emailBinance; // Email de Binance con el que se realizó el pago

	public String getEmailBinance() {
		return emailBinance;
	}

	public void setEmailBinance(String emailBinance) {
		this.emailBinance = emailBinance;
	}
}