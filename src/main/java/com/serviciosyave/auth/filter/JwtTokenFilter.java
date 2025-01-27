// package com.springboot.backend.andres.usersapp.usersbackend.auth.filter;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.web.filter.OncePerRequestFilter;
// import java.io.IOException;

// //@WebFilter("/api/*")  // Configura el filtro para el path que desees
// public class JwtTokenFilter extends OncePerRequestFilter {

//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//             throws ServletException, IOException {
//         System.out.println("JwtTokenFilter.JwtTokenFilter.JwtTokenFilter");
//         String token = request.getParameter("token");  // Aquí obtienes el token (puede ser de una cookie, sesión, etc.)

//         // Si el token no es nulo ni vacío, lo agregamos al request
//         if (token != null && !token.isEmpty()) {
//             request = new CustomHttpServletRequestWrapper(request, token);
//         }

//         // Continuamos con la cadena de filtros
//         filterChain.doFilter(request, response);
//     }
// }