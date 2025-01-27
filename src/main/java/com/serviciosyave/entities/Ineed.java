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

	    @ManyToOne 
	    @JoinColumn(name = "subcategory_id")
	    private Subcategory subcategory; 
	     
	    private String ubicacion;  
	    private LocalDateTime fechaHora;  
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

		public Subcategory getSubcategory() {
			return subcategory;
		}

		public void setSubcategory(Subcategory subcategory) {
			this.subcategory = subcategory;
		}

		public String getUbicacion() {
			return ubicacion;
		}

		public void setUbicacion(String ubicacion) {
			this.ubicacion = ubicacion;
		}

		public LocalDateTime getFechaHora() {
			return fechaHora;
		}

		public void setFechaHora(LocalDateTime fechaHora) {
			this.fechaHora = fechaHora;
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
}