package com.serviciosyave.auth.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.serviciosyave.entities.User;
import com.serviciosyave.services.CustomUserDetails;
import com.serviciosyave.services.JpaUserDetailsService;
import static com.serviciosyave.auth.TokenJwtConfig.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
   

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String username = null;
        String password = null;

        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);
        return this.authenticationManager.authenticate(authenticationToken);
    }

    @Override  
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,  
                                            Authentication authResult) throws IOException, ServletException {  
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();   
        String username = userDetails.getUsername();   
        Long userId = userDetails.getUserId();   
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();   
        boolean isAdmin = roles.stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));   

        // Crea los claims  
        Claims claims = Jwts  
                .claims()  
                .add("authorities", new ObjectMapper().writeValueAsString(roles))  
                .add("username", username)  
                .add("isAdmin", isAdmin)  
                .add("userId", userId)  
                .build();  
        
        String jwt = Jwts.builder()  
                .subject(username)  
                .claims(claims)  
                .signWith(SECRET_KEY)  
                .issuedAt(new Date())  
                .expiration(new Date(System.currentTimeMillis() + 3600000))  
                .compact();  

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + jwt);  

        // Crear cuerpo de respuesta  
        Map<String, Object> body = new HashMap<>();  
        body.put("token", jwt);  
        body.put("username", username);  
        body.put("message", String.format("Hola %s has iniciado sesión con éxito", username));  

        // Agregar detalles del usuario (se serializa manualmente)  
        Map<String, Object> userDetailsMap = new HashMap<>();  
        userDetailsMap.put("userId", userId);  
        userDetailsMap.put("username", username);  
        userDetailsMap.put("roles", roles); // Aquí podrías convertir roles a una lista de strings si es necesario  
        userDetailsMap.put("isAdmin", isAdmin);  

        body.put("userDetails", userDetailsMap); // Agrega los detalles creados  

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));  
        response.setContentType(CONTENT_TYPE);  
        response.setStatus(HttpServletResponse.SC_OK);  
    }

    // En el método unsuccessfulAuthentication
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        Map<String, Object> body = new HashMap<>();

        // Obtener datos del ThreadLocal
        String mensajeError = JpaUserDetailsService.getMensaje();
        String errorType = JpaUserDetailsService.getErrorType();
        Long userId = JpaUserDetailsService.getUserIdFromThread();

        if (errorType.equals("USER_NOT_FOUND")) {
            body.put("errorType", "USER_NOT_FOUND");
            body.put("message", "El usuario no existe");
        } else if (errorType.equals("EMAIL_NOT_VERIFIED")) {
            body.put("errorType", "EMAIL_NOT_VERIFIED");
            body.put("message", "Correo no verificado");
            if (userId != null) {
                body.put("userId", userId);
            }
        } else if (failed instanceof BadCredentialsException) {
            body.put("errorType", "INVALID_CREDENTIALS");
            body.put("message", "Usuario o contraseña incorrectos");
        } else {
            body.put("errorType", "UNKNOWN");
            body.put("message", "Error de autenticación");
        }

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Limpiar ThreadLocals después de usarlos
        JpaUserDetailsService.clearThreadLocals();
    }
}

