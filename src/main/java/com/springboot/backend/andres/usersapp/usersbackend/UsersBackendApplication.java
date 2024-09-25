package com.springboot.backend.andres.usersapp.usersbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springboot.backend.andres.usersapp.usersbackend.services.EmailService;
import org.springframework.boot.CommandLineRunner;  

import java.util.Scanner;  

@SpringBootApplication
public class UsersBackendApplication implements CommandLineRunner {

	 @Autowired
	    private PasswordEncoder passwordEncoder;
	 
	 @Autowired  
	    private EmailService emailService;
	 
	public static void main(String[] args) {
		SpringApplication.run(UsersBackendApplication.class, args);
		System.out.println("Ahora si levanta");
		
		String password = "123";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println("Contraseña codificada: " +password+ "es : "+ encodedPassword);
	}

	@Override
	public void run(String... args) {
		Scanner scanner = new Scanner(System.in);  
        System.out.print("Introduce un correo electrónico: ");  
        String email = scanner.nextLine();  
        //emailService.sendEmail(email); 
		
	}

}
