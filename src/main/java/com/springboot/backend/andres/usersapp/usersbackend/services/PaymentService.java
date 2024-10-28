package com.springboot.backend.andres.usersapp.usersbackend.services;

import org.springframework.stereotype.Service;
import com.springboot.backend.andres.usersapp.usersbackend.entities.Payment;
import com.springboot.backend.andres.usersapp.usersbackend.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;  

@Service
public class PaymentService {

	  @Autowired  
	    private PaymentRepository paymentRepository;  

	  // Método para guardar la ubicación  
	    public Payment createdPayment(Payment payment) {  
	        return paymentRepository.save(payment);  
	    }  
    
}  
