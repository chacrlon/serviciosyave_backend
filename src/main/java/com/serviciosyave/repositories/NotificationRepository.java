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
    @Query("UPDATE Notification n SET " +
            "n.message = :newMessage, " +
            "n.isRead = false, " +
            "n.paymentId = :paymentId " +
            "WHERE n.id = :id " +
            "AND n.estatus = 'Message' " +
            "AND n.paymentId = :paymentId")  // Asegura que solo actualice con mismo paymentId
    void updateExistingNotification(
            @Param("id") Long id,
            @Param("newMessage") String newMessage,
            @Param("paymentId") Long paymentId
    );

    @Modifying
    @Query("UPDATE Notification n SET n.status = :newStatus " +
            "WHERE (n.ineedId = :ineedId OR :ineedId IS NULL) " +
            "OR (n.vendorServiceId = :vendorServiceId OR :vendorServiceId IS NULL)")
    void updateStatusByIneedOrVendorService(
            @Param("ineedId") Long ineedId,
            @Param("vendorServiceId") Long vendorServiceId,
            @Param("newStatus") String newStatus
    );
    
    List<Notification> findByUserIdAndMessageAndIsRead(Long userId, String message, boolean isRead);

    Optional<Notification> findTopByUserIdAndUserId2AndEstatusOrderByIdDesc(Long userId, Long userId2, String estatus);

    @Query("SELECT n FROM Notification n " +
            "WHERE n.userId = :userId " +
            "AND n.userId2 = :userId2 " +
            "AND n.estatus = :estatus " +
            "AND n.paymentId = :paymentId " +  // Nuevo criterio
            "ORDER BY n.id DESC")
    Optional<Notification> findTopByUserIdAndUserId2AndEstatusAndPaymentIdOrderByIdDesc(
            @Param("userId") Long userId,
            @Param("userId2") Long userId2,
            @Param("estatus") String estatus,
            @Param("paymentId") Long paymentId
    );

}