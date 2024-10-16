package com.springboot.backend.andres.usersapp.usersbackend.services;  

import com.springboot.backend.andres.usersapp.usersbackend.entities.MobilePayment;
import com.springboot.backend.andres.usersapp.usersbackend.entities.User;
import com.springboot.backend.andres.usersapp.usersbackend.repositories.MobilePaymentRepository;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Service;  
import java.util.List;  
import java.util.Optional;  

@Service  
public class MobilePaymentService {  

    @Autowired  
    private MobilePaymentRepository mobilePaymentRepository;  

    public MobilePayment createMobilePayment(MobilePayment mobilePayment) {  
        return mobilePaymentRepository.save(mobilePayment);  
    }  

    public List<MobilePayment> getAllMobilePayments() {  
        return mobilePaymentRepository.findAll();  
    }  

    public Optional<MobilePayment> getMobilePaymentById(Long id) {  
        return mobilePaymentRepository.findById(id);  
    }  

    public MobilePayment updateMobilePayment(Long id, MobilePayment mobilePayment) {  
    	mobilePayment.setId(id);
        return mobilePaymentRepository.save(mobilePayment);  
    }  

    public void deleteMobilePayment(Long id) {  
        mobilePaymentRepository.deleteById(id);  
    }  
    
    
 // Método para buscar Binance por usuario  
    public Optional<MobilePayment> findMobilePaymentByUser(User user) {  
        return mobilePaymentRepository.findByUser(user);  
    }  

    // Nuevo método para buscar Binance por ID de usuario  
    public Optional<MobilePayment> findMobilePaymentByUserId(Long userId) {  
        return mobilePaymentRepository.findByUserId(userId);  
    }  
}