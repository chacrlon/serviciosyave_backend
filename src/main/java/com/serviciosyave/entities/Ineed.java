package com.serviciosyave.entities;   

import jakarta.persistence.*;  
import java.time.LocalDateTime;

@Entity   
public class Ineed{  
	    @Id  
	    @GeneratedValue(strategy = GenerationType.IDENTITY)  
	    private Long id;  
	    private String titulo;  
	    private String descripcion;  
	    
	    @ManyToOne
	    @JoinColumn(name = "category_id")
	    private Category category;

		//Formulario Dinamico
	    @Column(columnDefinition = "TEXT")
	    private String formularioData;

	    @ManyToOne 
	    @JoinColumn(name = "subcategory_id")
	    private Subcategory subcategory; 
	     
	    private String address;
		private double latitude;
		private double longitude;
		private double nearby;
	    private LocalDateTime fechaHora;
		private Boolean allowNegotiation;
	    private Double presupuesto;  
		private Long userId; 
		private Long professionalUserId; 

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getTitulo() {
			return titulo;
		}

		public void setTitulo(String titulo) {
			this.titulo = titulo;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}

	    public Category getCategory() {
			return category;
		}

		public void setCategory(Category category) {
			this.category = category;
		}

	public String getFormularioData() {
		return formularioData;
	}

	public void setFormularioData(String formularioData) {
		this.formularioData = formularioData;
	}

	public Subcategory getSubcategory() {
			return subcategory;
		}

		public void setSubcategory(Subcategory subcategory) {
			this.subcategory = subcategory;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
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

		public LocalDateTime getFechaHora() {
			return fechaHora;
		}

		public void setFechaHora(LocalDateTime fechaHora) {
			this.fechaHora = fechaHora;
		}

		public Boolean getAllowNegotiation() {
			return allowNegotiation;
		}

		public void setAllowNegotiation(Boolean allowNegotiation) {
			this.allowNegotiation = allowNegotiation;
		}

		public Double getPresupuesto() {
			return presupuesto;
		}

		public void setPresupuesto(Double presupuesto) {
			this.presupuesto = presupuesto;
		}

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public Long getProfessionalUserId() {
			return professionalUserId;
		}

		public void setProfessionalUserId(Long professionalUserId) {
			this.professionalUserId = professionalUserId;
		}

		public double getNearby() {
			return nearby;
		}

		public void setNearby(double nearby) {
			this.nearby = nearby;
		}
}