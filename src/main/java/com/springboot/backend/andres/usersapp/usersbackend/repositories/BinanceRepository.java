package com.springboot.backend.andres.usersapp.usersbackend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.backend.andres.usersapp.usersbackend.entities.Binance;
import com.springboot.backend.andres.usersapp.usersbackend.entities.User;

public interface BinanceRepository extends JpaRepository<Binance, Long>{

	@Override  
    Page<Binance> findAll(Pageable pageable);
	
}
