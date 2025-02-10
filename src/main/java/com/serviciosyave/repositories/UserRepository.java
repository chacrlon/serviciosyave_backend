package com.serviciosyave.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.serviciosyave.entities.User;
import com.serviciosyave.entities.UserStatus;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByUsername(String name);  
    
    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationCode(String code);  
    
    @Override  
    Page<User> findAll(Pageable pageable);  
     
    Page<User> findByVerificationCode(boolean verificationCode, Pageable pageable);  
    
    Optional<User> findByLastname(String lastName);  
    
    List<User> findByStatus(UserStatus status);  
}
