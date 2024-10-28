package com.springboot.backend.andres.usersapp.usersbackend.entities;  

public class ServiceFilter {  
	private String categoria;  
    private String subcategoria;  
    private Double minPrecio;  
    private Double maxPrecio;  
    private Boolean destacado;  
    private Boolean remoto;  
    private String textoLibre;  
    private Double latitude;  
    private Double longitude;  
    private Double distance;
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getSubcategoria() {
		return subcategoria;
	}
	public void setSubcategoria(String subcategoria) {
		this.subcategoria = subcategoria;
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