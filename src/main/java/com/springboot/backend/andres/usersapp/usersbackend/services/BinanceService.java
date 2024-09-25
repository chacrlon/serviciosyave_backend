package com.springboot.backend.andres.usersapp.usersbackend.services;  

import org.springframework.stereotype.Service;  
import com.springboot.backend.andres.usersapp.usersbackend.entities.Binance;
import com.springboot.backend.andres.usersapp.usersbackend.entities.User;
import com.springboot.backend.andres.usersapp.usersbackend.repositories.BinanceRepository;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
        binance.setId(id); // Asegúrate de que tu entidad Binance tenga un método setId  
        return binanceRepository.save(binance);  
    }  

    public void deleteBinance(Long id) {  
        binanceRepository.deleteById(id);  
    }  
}