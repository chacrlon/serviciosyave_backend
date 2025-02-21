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

    public Claims create(Claims claims) {
        return claimsRepository.save(claims);
    }

}