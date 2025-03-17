package com.serviciosyave.services;

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

import com.serviciosyave.entities.User;
import com.serviciosyave.repositories.UserRepository;

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

    // Agrega este campo para almacenar el ID temporalmente
    private static ThreadLocal<Long> threadLocalUserId = new ThreadLocal<>();

    // ThreadLocal para almacenar el tipo de error
    private static ThreadLocal<String> threadLocalErrorType = new ThreadLocal<>();

    public static void setErrorType(String errorType) {
        threadLocalErrorType.set(errorType);
    }

    public static String getErrorType() {
        return threadLocalErrorType.get();
    }

    public static void clearThreadLocals() {
        threadLocalMensaje.remove();
        threadLocalUserId.remove();
        threadLocalErrorType.remove(); // Limpiar el nuevo ThreadLocal
    }

    @Transactional(readOnly = true)  
    @Override  
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        setMensaje("Intentando cargar usuario: " + username);
        setErrorType("UNKNOWN"); // Reinicia el errorType

        Optional<User> optionalUser = repository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            String mensajeError = "[USER_NOT_FOUND] El usuario '" + username + "' no existe";
            setMensaje(mensajeError);
            setErrorType("USER_NOT_FOUND"); // Establecer tipo de error
            throw new UsernameNotFoundException(mensajeError);
        }

        User user = optionalUser.get();

        if (!user.isEmailVerified()) {
            String mensajeError = "[EMAIL_NOT_VERIFIED] Correo no verificado para " + username;
            setMensaje(mensajeError);
            setErrorType("EMAIL_NOT_VERIFIED"); // Establecer tipo de error
            threadLocalUserId.set(user.getId());
            throw new UsernameNotFoundException(mensajeError);
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

    // Agrega este método para obtener el ID
    public static Long getUserIdFromThread() {
        return threadLocalUserId.get();
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