package com.serviciosyave.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.serviciosyave.entities.MobilePayment;
import com.serviciosyave.entities.User;

public interface MobilePaymentRepository extends JpaRepository<MobilePayment, Long>{

    Optional<MobilePayment> findByUser(User user); // Método para buscar Binance por usuario   

    Optional<MobilePayment> findByUserId(Long userId); // Nuevo método para buscar Binance por ID de usuario 
    
}