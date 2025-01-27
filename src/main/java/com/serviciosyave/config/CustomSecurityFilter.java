// package config;

// import jakarta.servlet.ServletException;
// import jakarta.servlet.ServletResponse;
// import jakarta.servlet.http.HttpServletRequest;
// import org.springframework.security.access.SecurityMetadataSource;
// import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import java.io.IOException;

// import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import java.io.IOException;

// @Component
// public class CustomSecurityFilter extends OncePerRequestFilter {

//     private final AntPathRequestMatcher pathMatcher = new AntPathRequestMatcher("/chat-socket");

//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//             throws ServletException, IOException {
//         if (pathMatcher.matches(request)) {
//             String token = request.getParameter("token");
//             if (token == null || !validateToken(token)) {
//                 response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid or missing token");
//                 return;
//             }
//         }

//         // Continuar con el resto del filtro
//         filterChain.doFilter(request, response);
//     }

//     private boolean validateToken(String token) {
//         // Lógica para validar el token
//         return token.equals("valid-token"); // Ejemplo
//     }
// }
