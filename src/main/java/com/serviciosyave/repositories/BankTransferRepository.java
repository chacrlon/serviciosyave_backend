package com.serviciosyave.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.serviciosyave.entities.BankTransfer;
import com.serviciosyave.entities.User;

public interface BankTransferRepository extends JpaRepository<BankTransfer, Long>{
	
    Optional<BankTransfer> findByUser(User user); // Método para buscar Binance por usuario   

    Optional<BankTransfer> findByUserId(Long userId); // Nuevo método para buscar Binance por ID de usuario  

}