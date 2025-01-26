package com.serviciosyave.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.serviciosyave.entities.Ubication;

public interface UbicationRepository extends JpaRepository<Ubication, Long> {  
    
}