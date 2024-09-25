package com.springboot.backend.andres.usersapp.usersbackend.services;

import java.util.List;
import org.springframework.stereotype.Service;

import com.springboot.backend.andres.usersapp.usersbackend.entities.PaymentMethod;
import com.springboot.backend.andres.usersapp.usersbackend.entities.PaymentTransaction;
import com.springboot.backend.andres.usersapp.usersbackend.entities.User;
import com.springboot.backend.andres.usersapp.usersbackend.entities.VendorService;
import com.springboot.backend.andres.usersapp.usersbackend.repositories.PaymentTransactionRepository;
import com.springboot.backend.andres.usersapp.usersbackend.repositories.VendorServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;  

@Service
public class PaymentService {

	  @Autowired  
	    private PaymentTransactionRepository paymentTransactionRepository;  

	    public PaymentTransaction createPaymentTransaction(Double monto, String referencia, VendorService vendorService, User user, PaymentMethod paymentMethod) {  
	        PaymentTransaction transaction = new PaymentTransaction();  
	        transaction.setMonto(monto);  
	        transaction.setReferencia(referencia);  
	        transaction.setVendorService(vendorService);  
	        transaction.setUser(user);  
	        transaction.setPaymentMethod(paymentMethod);  
	        return paymentTransactionRepository.save(transaction);  
	    }  
    
}  
