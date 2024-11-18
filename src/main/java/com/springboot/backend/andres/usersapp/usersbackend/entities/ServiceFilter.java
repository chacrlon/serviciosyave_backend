package com.springboot.backend.andres.usersapp.usersbackend.entities;  

public class ServiceFilter {  
	  private Long categoryId; // Cambiado a Long para ID  
	    private Long subcategoryId; // Cambiado a Long para ID  
	    private Double minPrecio;  
	    private Double maxPrecio;  
	    private Boolean destacado;  
	    private Boolean remoto;  
	    private String textoLibre;  
	    private Double latitude;  
	    private Double longitude;  
	    private Double distance;
		public Long getCategoryId() {
			return categoryId;
		}
		public void setCategoryId(Long categoryId) {
			this.categoryId = categoryId;
		}
		public Long getSubcategoryId() {
			return subcategoryId;
		}
		public void setSubcategoryId(Long subcategoryId) {
			this.subcategoryId = subcategoryId;
		}
		public Double getMinPrecio() {
			return minPrecio;
		}
		public void setMinPrecio(Double minPrecio) {
			this.minPrecio = minPrecio;
		}
		public Double getMaxPrecio() {
			return maxPrecio;
		}
		public void setMaxPrecio(Double maxPrecio) {
			this.maxPrecio = maxPrecio;
		}
		public Boolean getDestacado() {
			return destacado;
		}
		public void setDestacado(Boolean destacado) {
			this.destacado = destacado;
		}
		public Boolean getRemoto() {
			return remoto;
		}
		public void setRemoto(Boolean remoto) {
			this.remoto = remoto;
		}
		public String getTextoLibre() {
			return textoLibre;
		}
		public void setTextoLibre(String textoLibre) {
			this.textoLibre = textoLibre;
		}
		public Double getLatitude() {
			return latitude;
		}
		public void setLatitude(Double latitude) {
			this.latitude = latitude;
		}
		public Double getLongitude() {
			return longitude;
		}
		public void setLongitude(Double longitude) {
			this.longitude = longitude;
		}
		public Double getDistance() {
			return distance;
		}
		public void setDistance(Double distance) {
			this.distance = distance;
		}  

	    
	    
    
    
}