package com.springboot.backend.andres.usersapp.usersbackend.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.backend.andres.usersapp.usersbackend.entities.BankTransfer;
import com.springboot.backend.andres.usersapp.usersbackend.entities.User;

public interface BankTransferRepository extends JpaRepository<BankTransfer, Long>{
	
    Optional<BankTransfer> findByUser(User user); // Método para buscar Binance por usuario   

    Optional<BankTransfer> findByUserId(Long userId); // Nuevo método para buscar Binance por ID de usuario  

}
