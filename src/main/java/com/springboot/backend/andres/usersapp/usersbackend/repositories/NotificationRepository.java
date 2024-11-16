package com.springboot.backend.andres.usersapp.usersbackend.repositories;  

import org.springframework.data.jpa.repository.JpaRepository;  
import com.springboot.backend.andres.usersapp.usersbackend.entities.Notification; 
import java.util.List;  

public interface NotificationRepository extends JpaRepository<Notification, Long> {  
    List<Notification> findByUserId(Long userId);  
    
    List<Notification> findByUserIdAndMessageAndIsRead(Long userId, String message, boolean isRead);
    
}