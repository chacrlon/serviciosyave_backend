package com.serviciosyave.entities;  

import jakarta.persistence.*;  

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public class Payment {  
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;  
    private Double monto;  
    private String divisa;
    private String metodoPago;
    private String referencia;   
    private String estatus;
    private String TipoPago;

    @Column(name = "vendor_service_id") 
    private Long vendorServiceId;

    @Column(name = "ineed_id")
    private Long ineedId;

    @Column(name = "users_id")   
    private Long usersId;

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

    public String getDivisa() {  
        return divisa;  
    }  

    public void setDivisa(String divisa) {  
        this.divisa = divisa;  
    }  
 

    public String getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
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

    public Long getUsersId() {
        return usersId;
    }  

    public void setUsersId(Long usersId) {
        this.usersId = usersId;
    }

    public Long getIneedId() {
        return ineedId;
    }

    public void setIneedId(Long ineedId) {
        this.ineedId = ineedId;
    }


    public String getEstatus() {  
        return estatus;  
    }  

    public void setEstatus(String estatus) {  
        this.estatus = estatus;  
    }

	public String getTipoPago() {
		return TipoPago;
	}

	public void setTipoPago(String tipoPago) {
		TipoPago = tipoPago;
	} 
    
    

}