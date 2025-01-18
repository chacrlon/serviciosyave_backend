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

    private static ThreadLocal<String> threadLocalMensaje = ThreadLocal.withInitial(() -> ""); // Inicializa el mensaje por hilo  

    public static void setMensaje(String nuevoMensaje) {  
        threadLocalMensaje.set(nuevoMensaje);  
    }  

    public static String getMensaje() {  
        return threadLocalMensaje.get();  
    }    

    @Transactional(readOnly = true)  
    @Override  
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {  
        // Establecer un mensaje inicial si es necesario  
        setMensaje("Intentando cargar usuario por el nombre de usuario: " + username);  
        System.out.println(getMensaje()); // Imprimir mensaje inicial  

        Optional<User> optionalUser = repository.findByUsername(username);  

        if (optionalUser.isEmpty()) {  
            // Establecer mensaje de error  
            setMensaje("No se encontró el usuario: " + username);  
            System.out.println(getMensaje());  // Imprimir mensaje de error  
            throw new UsernameNotFoundException(getMensaje());  
        }  

        User user = optionalUser.get();  
        System.out.println("Usuario encontrado: " + username);  

        // Validar si el email está verificado  
        if (!user.isEmailVerified()) {  
            setMensaje("El correo electrónico del usuario " + username + " no ha sido verificado.");  
            System.out.println(getMensaje()); // Imprimir mensaje de verificación  
            throw new UsernameNotFoundException(getMensaje());  
        }  

        List<GrantedAuthority> authorities = user.getRoles()  
                .stream()  
                .map(role -> new SimpleGrantedAuthority(role.getName()))  
                .collect(Collectors.toList());  

        System.out.println("Autorizaciones concedidas para el usuario " + username + ": " + authorities);  

        CustomUserDetails userDetails = new CustomUserDetails(username,  
                user.getPassword(),  
                authorities,  
                user.getId(),  
                user.isEmailVerified(),  
                getMensaje()  // Aquí también puedes establecer el mensaje si es necesario  
        );  

        System.out.println("Login exitoso para el usuario: " + username);  
        return userDetails;   
    }  

    public Long getUserId() {  
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {  
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();  
            return userDetails.getUserId();   
        }  
        return null;   
    }  

    public User findById(Long id) {  
        return repository.findById(id).orElse(null);  
    }  
}