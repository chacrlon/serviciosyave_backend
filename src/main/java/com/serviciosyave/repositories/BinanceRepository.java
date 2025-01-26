package com.serviciosyave.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.serviciosyave.entities.Binance;
import com.serviciosyave.entities.User;

public interface BinanceRepository extends JpaRepository<Binance, Long> {  
    
    @Override  
    Page<Binance> findAll(Pageable pageable);  

    Optional<Binance> findByUser(User user); // Método para buscar Binance por usuario   

    Optional<Binance> findByUserId(Long userId); // Nuevo método para buscar Binance por ID de usuario  

}