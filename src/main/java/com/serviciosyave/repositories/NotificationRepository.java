package com.serviciosyave.repositories;  

import org.springframework.data.jpa.repository.JpaRepository;  
import com.serviciosyave.entities.Notification; 
import java.util.List;  

public interface NotificationRepository extends JpaRepository<Notification, Long> {  
    List<Notification> findByUserId(Long userId);  
    
    List<Notification> findByUserIdAndMessageAndIsRead(Long userId, String message, boolean isRead);
    
}