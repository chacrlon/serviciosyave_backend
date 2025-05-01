package com.serviciosyave.entities;  

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;  
import jakarta.persistence.GenerationType;  
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;  
import lombok.Data;  

@Entity  
@Table(name = "vendorService")  
@Data   
public class VendorService {
	
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;  
    private String nombre;  
    private String descripcion;  
    private Double precio;  
    private Boolean destacado;   

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;  

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;  

    private Boolean remoto;  
    private Boolean allowNegotiation;  
    private double latitude;  
    private double longitude;
	private double nearby;
	private String address;

    @Column(name = "users_id")   
    private Long userId;
   
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Boolean getDestacado() {
		return destacado;
	}

	public void setDestacado(Boolean destacado) {
		this.destacado = destacado;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Subcategory getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(Subcategory subcategory) {
		this.subcategory = subcategory;
	}

	public Boolean getRemoto() {
		return remoto;
	}

	public void setRemoto(Boolean remoto) {
		this.remoto = remoto;
	}

	public Boolean getAllowNegotiation() {
		return allowNegotiation;
	}

	public void setAllowNegotiation(Boolean allowNegotiation) {
		this.allowNegotiation = allowNegotiation;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getNearby() {
		return nearby;
	}

	public void setNearby(double nearby) {
		this.nearby = nearby;
	}
}