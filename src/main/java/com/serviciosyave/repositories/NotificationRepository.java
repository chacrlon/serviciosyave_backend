package com.serviciosyave.repositories;  

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.serviciosyave.entities.Notification; 
import java.util.List;
import java.util.Optional;  

public interface NotificationRepository extends JpaRepository<Notification, Long> {  
    List<Notification> findByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE Notification n SET n.message = :newMessage, n.isRead = false WHERE n.id = :id AND n.estatus = 'Message'")
    void updateExistingNotification(
        @Param("id") Long id,
        @Param("newMessage") String newMessage
    );
    
    List<Notification> findByUserIdAndMessageAndIsRead(Long userId, String message, boolean isRead);

    Optional<Notification> findTopByUserIdAndUserId2AndEstatusOrderByIdDesc(Long userId, Long userId2, String estatus);

}