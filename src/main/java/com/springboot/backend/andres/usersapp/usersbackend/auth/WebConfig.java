package com.springboot.backend.andres.usersapp.usersbackend.auth;
 
import org.springframework.context.annotation.Configuration;  
import org.springframework.web.servlet.config.annotation.CorsRegistry;  
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;  

@Configuration  
public class WebConfig implements WebMvcConfigurer {  
    @Override  
    public void addCorsMappings(CorsRegistry registry) {  
        registry.addMapping("/api/**")  
                .allowedOrigins("http://localhost:4200") // Cambia esto por la URL de tu frontend  
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  
                .allowedHeaders("*")  
                .allowCredentials(true);  
    }  
}