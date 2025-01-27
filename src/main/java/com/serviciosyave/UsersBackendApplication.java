package com.serviciosyave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.serviciosyave.services.EmailService;
import org.springframework.boot.CommandLineRunner;  

import javax.net.ssl.SSLContext;  
import javax.net.ssl.TrustManagerFactory;  
import java.io.FileInputStream;  
import java.security.KeyStore;  
import java.util.Scanner;  

@SpringBootApplication  
public class UsersBackendApplication implements CommandLineRunner {  

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired  
    private EmailService emailService;  

    public static void main(String[] args) {  
        // Configurar el trustStore antes de iniciar la aplicación  
        configureTrustStore();  
        SpringApplication.run(UsersBackendApplication.class, args);  
    }  

    private static void configureTrustStore() {  
        try {  
            // Ruta al trustStore y contraseña  
            String trustStorePath = "C:/Program Files/Java/jdk-20/lib/security/cacerts"; // Cambia la ruta según sea necesario  
            String trustStorePassword = "changeit";

            // Cargar el trustStore  
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            try (FileInputStream trustStoreStream = new FileInputStream(trustStorePath)) {
                trustStore.load(trustStoreStream, trustStorePassword.toCharArray());
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());  
            trustManagerFactory.init(trustStore);  

            SSLContext sslContext = SSLContext.getInstance("TLS");  
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);  
            SSLContext.setDefault(sslContext);  

        } catch (Exception e) {  
            e.printStackTrace();  
            // Manejo de errores: podrías lanzar una excepción o registrar el error  
        }  
    }  

    @Override  
    public void run(String... args) {  
        // Scanner scanner = new Scanner(System.in);  
        // System.out.print("Introduce un correo electrónico: ");  
        // String email = scanner.nextLine();  
        // emailService.sendEmail(email);  
    }  
}