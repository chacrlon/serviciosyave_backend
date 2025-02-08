package com.serviciosyave.entities;  

import jakarta.persistence.*;  

@Entity
@DiscriminatorValue("BANK_TRANSFER")
public class bankTransferPayment extends Payment {
    private String numeroCuenta; // Número de cuenta con el que se realizó la transferencia

    // Getters y setters
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
}