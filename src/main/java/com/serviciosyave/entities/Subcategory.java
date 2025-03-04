package com.serviciosyave.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity   
public class Subcategory{  
	
	@Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;  

    private String name;  

    @ManyToOne  
    @JoinColumn(name = "category_id")
    @JsonBackReference // Indica que esta es la propiedad inversa 
    private Category category;

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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}    
    
	    
}