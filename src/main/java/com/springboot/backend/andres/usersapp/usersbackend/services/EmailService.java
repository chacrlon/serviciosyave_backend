package com.springboot.backend.andres.usersapp.usersbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;  
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;  

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



/*    public void send(String from, String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(from);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    mailSender.send(message);
}

public void sendWithAttach(String from, String to, String subject,
                           String text, String attachName,
                           InputStreamSource inputStream) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setFrom(from);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(text, true);
    helper.addAttachment(attachName, inputStream);
    mailSender.send(message);
}*/