package com.springboot.backend.andres.usersapp.usersbackend.auth.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.backend.andres.usersapp.usersbackend.entities.User;
import com.springboot.backend.andres.usersapp.usersbackend.services.CustomUserDetails;
import com.springboot.backend.andres.usersapp.usersbackend.services.JpaUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static com.springboot.backend.andres.usersapp.usersbackend.auth.TokenJwtConfig.*;

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

    @Override  
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,  
                                              AuthenticationException failed) throws IOException, ServletException {  
        String mensaje = JpaUserDetailsService.getMensaje(); // Obtener el mensaje del hilo actual  

        // Crear cuerpo de respuesta  
        Map<String, String> body = new HashMap<>();  
        body.put("message", mensaje);  
        body.put("error", failed.getMessage());  

        // Enviar respuesta  
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));  
        response.setContentType(CONTENT_TYPE);  
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  
    }  
}

