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

    // Constructor
    public CustomUserDetails(String username, String password, List<GrantedAuthority> authorities, Long userId) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.userId = userId;
    }

    // Implementa los métodos de UserDetails
    public Long getUserId() {
        return userId; // Devuelve el ID del usuario
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // Retorna las autoridades del usuario
    }

    @Override
    public String getPassword() {
        return password; // Retorna la contraseña del usuario
    }

    @Override
    public String getUsername() {
        return username; // Retorna el nombre de usuario
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Puedes personalizar esta lógica según tu necesidad
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Puedes personalizar esta lógica según tu necesidad
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Puedes personalizar esta lógica según tu necesidad
    }

    @Override
    public boolean isEnabled() {
        return true; // Puedes personalizar esta lógica según tu necesidad
    }
}
