package com.serviciosyave.repositories;

import com.serviciosyave.entities.Claims;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClaimsRepository extends JpaRepository<Claims, Long>{

}