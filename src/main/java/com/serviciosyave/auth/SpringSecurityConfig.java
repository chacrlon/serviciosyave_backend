package com.serviciosyave.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.serviciosyave.auth.filter.JwtAuthenticationFilter;
import com.serviciosyave.auth.filter.JwtValidationFilter;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.POST, "/register/**").permitAll()
                .requestMatchers("/register/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/users", "/api/users/page/{page}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/binance/").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/binance/create").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/sellers/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/usuarios/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/sellers/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/sellers/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/sellers/**").permitAll()
                        // Permitir acceso a todos los endpoints de Bank Transfer
                .requestMatchers(HttpMethod.POST, "/api/banktransfer/create").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/banktransfer/").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/payment/all").permitAll()


                        // Permitir acceso a todos los endpoints de Mobile Payment
                .requestMatchers(HttpMethod.POST, "/api/mobilepayment/create").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/mobilepayment/").permitAll()

             // Permitir acceso a todos los endpoints de Service
                .requestMatchers("/api/service/**").permitAll()  // <--- Añadir esta línea
                .requestMatchers("/api/currency/dolar/**").permitAll()
                .requestMatchers("/chat-socket/**").permitAll()
                .requestMatchers("/api/categories/**").permitAll()
                .requestMatchers("/api/payment/**").permitAll()
                .requestMatchers("/api/payment/**").permitAll()
                .requestMatchers("/api/users/**").permitAll()
                .requestMatchers("/api/notifications/**").permitAll()
                        .requestMatchers("/notifications/**").permitAll()
                        .requestMatchers("/api/negotiations/**").permitAll()

                // Otras configuraciones para usuarios y administradores
                .requestMatchers(HttpMethod.GET, "/register/{id}").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")
                .anyRequest().authenticated())
                .cors(cors -> cors.configurationSource(configurationSource()))
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationManager(), UsernamePasswordAuthenticationFilter.class))
                .csrf(config -> config.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    CorsConfigurationSource configurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        config.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<CorsFilter>(
                new CorsFilter(this.configurationSource()));
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
    }
}