package com.serviciosyave.entities;  

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity   
public class Category{  
	
	@Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
	private Long id;  

	private String name;  

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)  
    @JsonManagedReference // Indica que este es el lado administrador de la relación  
	private List<Subcategory> subcategories;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Subcategory> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(List<Subcategory> subcategories) {
		this.subcategories = subcategories;
	}  
    
	
}