package com.serviciosyave.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.serviciosyave.entities.Seller;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;


public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByUserId(Long userId);

    // Ejemplo de consulta para proveedores cerca de una necesidad (Ineed)
    @Query("SELECT s FROM Seller s " +
            "JOIN s.selectedSubcategories sub " + // Usa selectedSubcategories o subcategories
            "WHERE sub.id = :subcategoryId " +
            "AND (6371 * acos(" +
            "cos(radians(:clientLat)) * cos(radians(s.latitude)) * " +
            "cos(radians(s.longitude) - radians(:clientLng)) + " +
            "sin(radians(:clientLat)) * sin(radians(s.latitude))" +
            ") <= s.coverageRadius)")
    List<Seller> findNearbyProvidersBySubcategory(
            @Param("subcategoryId") Long subcategoryId,
            @Param("clientLat") double clientLat,
            @Param("clientLng") double clientLng
    );
}
