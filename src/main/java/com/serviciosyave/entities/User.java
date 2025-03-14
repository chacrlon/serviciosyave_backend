package com.serviciosyave.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.serviciosyave.models.IUser;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "users")
@Data 
public class User implements IUser {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

	@Enumerated(EnumType.STRING)
	private PaymentMethodSelected paymentMethodSelected;

	@Column(columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal userMoney = BigDecimal.ZERO;

    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @NotEmpty
    @Email
    private String email;

    @NotBlank
    @Size(min=4, max = 12)
    private String username;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

    @NotBlank
    private String password;
    
    @Enumerated(EnumType.STRING) // Debes asegurarte de que el enum se almacene como cadena en la base de datos  
    private UserStatus status;  // "OCUPADO" o "NO_OCUPADO" 

    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name="users_roles",
        joinColumns = {@JoinColumn(name="user_id")},
        inverseJoinColumns = @JoinColumn(name="role_id"),
        uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "role_id"})}
    )
    private List<Role> roles;
     
    private String phone;  
    private String cedula;  
    private String birthdate;    
    private boolean isVendor; // true si es vendedor, false si es comprador    
    private boolean isEmailVerified; // true si esta verificado, false si no esta verificado  
    private String verificationCode; // código de verificación para el correo   
    
    @OneToMany(mappedBy = "userId") // Cambiatelo a userId  
    private List<VendorService> vendorServices;  
    
    @OneToOne(cascade = CascadeType.ALL)  
    @JoinColumn(name = "ubicacion_id", referencedColumnName = "id")  
    private Ubication ubicacion;

	private double rating = 0.0;

 // Constructor por defecto  
    public User() {  
        this.roles = new ArrayList<>();  
        this.status = UserStatus.NO_OCUPADO;  // Asigna el valor por defecto  
    }  

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
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCedula() {
		return cedula;
	}
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public boolean isVendor() {
		return isVendor;
	}
	public void setVendor(boolean isVendor) {
		this.isVendor = isVendor;
	}
	public boolean isEmailVerified() {
		return isEmailVerified;
	}
	public void setIsEmailVerified(boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}
	public String getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

	public List<VendorService> getVendorServices() {
		return vendorServices;
	}

	public void setVendorServices(List<VendorService> vendorServices) {
		this.vendorServices = vendorServices;
	}

	public Ubication getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Ubication ubicacion) {
		this.ubicacion = ubicacion;
	}

	public void setEmailVerified(boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public BigDecimal getUserMoney() {
		return userMoney;
	}

	public void setUserMoney(BigDecimal userMoney) {
		this.userMoney = userMoney;
	}

	public PaymentMethodSelected getPaymentMethodSelected() {
		return paymentMethodSelected;
	}

	public void setPaymentMethodSelected(PaymentMethodSelected paymentMethodSelected) {
		this.paymentMethodSelected = paymentMethodSelected;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
}