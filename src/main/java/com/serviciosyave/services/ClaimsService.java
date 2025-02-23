package com.serviciosyave.services;

import com.serviciosyave.entities.Claims;
import com.serviciosyave.repositories.ClaimsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service  
public class ClaimsService {

    @Autowired  
    private ClaimsRepository claimsRepository;

    public Claims save(Claims claims) {
        return claimsRepository.save(claims);
    }

    public Optional<Claims> getClaim(Long id) {
        return claimsRepository.findById(id);
    }

}