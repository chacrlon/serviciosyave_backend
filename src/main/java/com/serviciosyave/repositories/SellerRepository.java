package com.serviciosyave.repositories;

import org.springframework.data.jpa.repository.JpaRepository;  
import com.serviciosyave.entities.Seller;  

public interface SellerRepository extends JpaRepository<Seller, Long> {  
   
}
