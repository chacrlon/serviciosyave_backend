package com.serviciosyave.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import com.serviciosyave.entities.User;
import com.serviciosyave.entities.UserStatus;
import com.serviciosyave.entities.VendorService;
import com.serviciosyave.models.UserRequest;

public interface UserService {

    List<User> findAll();

    Page<User> findAll(Pageable pageable);

    Optional<User> findById(@NonNull Long id);

    Optional<User> findByUserEmail(@NonNull String email);

    User save(User user);

    Optional<User> update(UserRequest user, Long id);

    void deleteById(Long id);
    
    List<VendorService> findServicesByUserId(Long userId);
    
    Optional<User> updateUserStatus(Long id, UserStatus status); 
}