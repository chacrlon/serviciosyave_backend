package com.serviciosyave.services;

import org.springframework.stereotype.Service;
import com.serviciosyave.entities.Binance;
import com.serviciosyave.entities.User;
import com.serviciosyave.repositories.BinanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service  
public class BinanceService {  

    @Autowired  
    private BinanceRepository binanceRepository;   

    public Binance createBinance(Binance binance) {  
        return binanceRepository.save(binance);  
    }  

    public List<Binance> getAllBinances() {  
        return binanceRepository.findAll();  
    }  

    public Optional<Binance> getBinanceById(Long id) {  
        return binanceRepository.findById(id);  
    }  

    public Binance updateBinance(Long id, Binance binance) {  
        binance.setId(id);  
        return binanceRepository.save(binance);  
    }  

    public void deleteBinance(Long id) {  
        binanceRepository.deleteById(id);  
    }  

    // Método para buscar Binance por usuario  
    public Optional<Binance> findBinanceByUser(User user) {  
        return binanceRepository.findByUser(user);  
    }  

    // Nuevo método para buscar Binance por ID de usuario  
    public Optional<Binance> findBinanceByUserId(Long userId) {  
        return binanceRepository.findByUserId(userId);  
    }  
}