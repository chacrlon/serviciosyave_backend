package com.springboot.backend.andres.usersapp.usersbackend.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {  
    private String username;  
    private String password;  
    private List<GrantedAuthority> authorities;  
    private Long userId; // Almacena el ID del usuario  
    private boolean emailVerified; // Almacena si el correo electrónico está verificado  

    // Constructor  
    public CustomUserDetails(String username, String password, List<GrantedAuthority> authorities, Long userId, boolean emailVerified) {  
        this.username = username;  
        this.password = password;  
        this.authorities = authorities;  
        this.userId = userId;  
        this.emailVerified = emailVerified; // Almacena el estado de verificación  
    }  

    // Implementa los métodos de UserDetails  
    public Long getUserId() {  
        return userId;  
    }  

    public boolean isEmailVerified() {  
        return emailVerified; // Método para obtener el estado de verificación  
    }  

    @Override  
    public Collection<? extends GrantedAuthority> getAuthorities() {  
        return authorities;  
    }  

    @Override  
    public String getPassword() {  
        return password;  
    }  

    @Override  
    public String getUsername() {  
        return username;  
    }  

    @Override  
    public boolean isAccountNonExpired() {  
        return true;  
    }  

    @Override  
    public boolean isAccountNonLocked() {  
        return true;  
    }  

    @Override  
    public boolean isCredentialsNonExpired() {  
        return true;  
    }  

    @Override  
    public boolean isEnabled() {  
        return true;  
    }  
}