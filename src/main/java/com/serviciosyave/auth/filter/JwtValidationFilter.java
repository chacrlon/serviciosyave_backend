package com.serviciosyave.auth.filter;

import static com.serviciosyave.auth.TokenJwtConfig.*;

import com.fasterxml.jackson.core.JsonParser;
import com.serviciosyave.auth.SimpleGrantedAuthorityJsonCreator;

import java.io.IOException;
import java.util.*;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidationFilter extends BasicAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtValidationFilter(AuthenticationManager authenticationManager, Class<UsernamePasswordAuthenticationFilter> usernamePasswordAuthenticationFilterClass) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = null;

        if(request.getRequestURI().contains("/?token")) {
//            chain.doFilter(request, response);
//            return;
            System.out.println("CHAT-SOCKET METHOD "+request.getMethod());
            System.out.println("CHAT-SOCKET getRequestURI "+ request.getRequestURI());
            System.out.println("CHAT-SOCKET getParameter "+ request.getParameter("token"));

            HttpServletRequest wrappedRequest = new CustomHttpServletRequestWrapper(request, request.getParameter("token"));

            header = wrappedRequest.getHeader(HEADER_AUTHORIZATION);

        } else {

            header = request.getHeader(HEADER_AUTHORIZATION);
        }

        System.out.println("CHAT-SOCKET header "+header);

        if (header == null || !header.startsWith(PREFIX_TOKEN)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(PREFIX_TOKEN, "");

        try {
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build()
            .parseSignedClaims(token).getPayload();

            String username = claims.getSubject();
            Long userId = claims.get("userId", Long.class);
            Object authoritiesClaims = claims.get("authorities");
            Collection<? extends GrantedAuthority> roles = null;
            Collection<? extends GrantedAuthority> rolEmpty = Collections.emptyList();

            System.out.println("CHAT-SOCKET authoritiesClaims "+authoritiesClaims);
            System.out.println("CHAT-SOCKET token "+token);

            if(authoritiesClaims != null) {
                roles = Arrays.asList(new ObjectMapper()
                        .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                        .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authoritiesClaims != null ? roles : rolEmpty);

            authenticationToken.setDetails(userId); // Almacena el userId

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response);

        } catch (JwtException e) {
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "El token es invalido!");

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(401);
            response.setContentType(CONTENT_TYPE);
        }
    }
}