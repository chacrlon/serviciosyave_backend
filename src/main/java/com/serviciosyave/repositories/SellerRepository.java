package com.serviciosyave.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.serviciosyave.entities.Seller;


import java.util.Optional;


public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByUserId(Long userId);
}
