package com.springboot.backend.andres.usersapp.usersbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;  
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service; 

@Service
public class EmailService {

	@Autowired  
    private JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {  
        this.mailSender = mailSender;  
    }  

    public boolean sendEmail(String toEmail, String subject, String text) {  
        SimpleMailMessage message = new SimpleMailMessage();  
        message.setTo(toEmail);  
        message.setSubject(subject);  
        message.setText(text);  
        
        try {  
            mailSender.send(message);  
            System.out.println("Correo enviado a: " + toEmail);  
            return true;  // Retorna verdadero si se envió exitosamente  
        } catch (Exception e) {  
            System.out.println("Error al enviar el correo: " + e.getMessage());  
            return false;  // Retorna falso si hubo un error al enviar  
        }  
    }
}

