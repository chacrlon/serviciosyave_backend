package com.serviciosyave.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.serviciosyave.entities.Ineed;

public interface IneedRepository extends JpaRepository<Ineed, Long>{
	
}
