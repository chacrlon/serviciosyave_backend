package com.springboot.backend.andres.usersapp.usersbackend.services;  

import java.util.List;  
import java.util.Optional;  
import java.util.stream.Collectors;  

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.security.core.GrantedAuthority;  
import org.springframework.security.core.authority.SimpleGrantedAuthority;  
import org.springframework.security.core.userdetails.UserDetails;  
import org.springframework.security.core.userdetails.UserDetailsService;  
import org.springframework.security.core.userdetails.UsernameNotFoundException;  
import org.springframework.security.core.Authentication;  
import org.springframework.security.core.context.SecurityContextHolder;  
import org.springframework.stereotype.Service;  
import org.springframework.transaction.annotation.Transactional;  

import com.springboot.backend.andres.usersapp.usersbackend.entities.User;  
import com.springboot.backend.andres.usersapp.usersbackend.repositories.UserRepository;  


@Service  
public class JpaUserDetailsService implements UserDetailsService {  

    @Autowired  
    private UserRepository repository;  

    private Long userIdd; // Variable de instancia
    private String hola; // Variable de instancia

    @Transactional(readOnly = true)  
    @Override  
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {  

        Optional<User> optionalUser = repository.findByUsername(username);  

        if (optionalUser.isEmpty()) {  
            throw new UsernameNotFoundException(String.format("Username %s no existe en el sistema", username));  
        }  

        User user = optionalUser.get();  

        userIdd = user.getId(); // Asigna el valor a la variable de instancia
String hola = " holiwirivirivis ";
        System.out.println("el userid es : " + userIdd);
        List<GrantedAuthority> authorities = user.getRoles()  
                .stream()  
                .map(role -> new SimpleGrantedAuthority(role.getName()))  
                .collect(Collectors.toList());  

        // Crear y retornar el CustomUserDetails  
        return new CustomUserDetails(username,  
                user.getPassword(),  
                authorities,  
                user.getId()); 
    }  

    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUserId(); // Esto obtiene el ID del usuario autenticado
        }
        return null; // Retorna null si no hay un usuario autenticado
    }

    
  
}
