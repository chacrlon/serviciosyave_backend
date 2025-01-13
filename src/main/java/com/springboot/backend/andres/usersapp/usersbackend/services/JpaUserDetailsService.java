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

    @Transactional(readOnly = true)  
    @Override  
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {  
        System.out.println("Intentando cargar usuario por el nombre de usuario: " + username);  
        
        Optional<User> optionalUser = repository.findByUsername(username);  

        if (optionalUser.isEmpty()) {  
            System.out.println("No se encontró el usuario: " + username);  
            throw new UsernameNotFoundException(String.format("Username %s no existe en el sistema", username));  
        }  

        User user = optionalUser.get();  
        System.out.println("Usuario encontrado: " + username);  
        
        // Validar si el email está verificado  
        if (!user.isEmailVerified()) {  
            System.out.println("El correo electrónico del usuario " + username + " no ha sido verificado.");  
            throw new UsernameNotFoundException("El correo electrónico no ha sido verificado.");  
        }  

        List<GrantedAuthority> authorities = user.getRoles()  
                .stream()  
                .map(role -> new SimpleGrantedAuthority(role.getName()))  
                .collect(Collectors.toList());  

        System.out.println("Autorizaciones concedidas para el usuario " + username + ": " + authorities);  
        // Crear y retornar el CustomUserDetails incluyendo isEmailVerified  
        CustomUserDetails userDetails = new CustomUserDetails(username,  
                user.getPassword(),  
                authorities,  
                user.getId(),  // ID del usuario  
                user.isEmailVerified()); // Estado de verificación del email  
        
        System.out.println("Login exitoso para el usuario: " + username);  
        return userDetails;  
    }

    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUserId(); // Esto obtiene el ID del usuario autenticado
        }
        return null; // Retorna null si no hay un usuario autenticado
    }

 // Nuevo método para buscar un usuario por ID  
    public User findById(Long id) {  
        return repository.findById(id).orElse(null); // Asegúrate de que este método esté definido en tu UserRepository  
    } 
  
}
