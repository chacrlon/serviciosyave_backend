package com.springboot.backend.andres.usersapp.usersbackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.backend.andres.usersapp.usersbackend.entities.MobilePayment;
import com.springboot.backend.andres.usersapp.usersbackend.entities.User;

public interface MobilePaymentRepository extends JpaRepository<MobilePayment, Long>{

    Optional<MobilePayment> findByUser(User user); // Método para buscar Binance por usuario   

    Optional<MobilePayment> findByUserId(Long userId); // Nuevo método para buscar Binance por ID de usuario 
    
}
