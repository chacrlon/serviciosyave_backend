package com.serviciosyave.repositories;

import com.serviciosyave.entities.Negotiate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface NegotiateRepository extends JpaRepository<Negotiate, Long> {
    // Método para contar ofertas en un hilo
    int countByThreadId(String threadId);

    // Método para obtener historial de negociaciones
    List<Negotiate> findByThreadIdOrderByTimestampDesc(String threadId);

    Optional<Negotiate> findById(Long id);

    Optional<Negotiate> findByIneedIdAndReceiverIdAndSenderId(Long ineedId, Long receiverId, Long senderId);

    Optional<Negotiate> findByVendorServiceIdAndReceiverIdAndSenderId(Long vendorServiceId, Long receiverId, Long senderId);

    // Consulta actualizada con relaciones JPA
    @Query("SELECT n FROM Negotiate n WHERE n.ineed.id = :ineedId AND n.threadId LIKE %:threadIdPattern%")
    List<Negotiate> findAllThreadsForIneed(@Param("ineedId") Long ineedId,
                                           @Param("threadIdPattern") String threadIdPattern);
}