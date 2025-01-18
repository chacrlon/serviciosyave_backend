package com.springboot.backend.andres.usersapp.usersbackend.services;  

import org.springframework.stereotype.Component;  

@Component  
public class MessageStore {  
    private String mensaje;  

    public String getMensaje() {  
        return mensaje;  
    }  

    public void setMensaje(String mensaje) {  
        this.mensaje = mensaje;  
    }  
}