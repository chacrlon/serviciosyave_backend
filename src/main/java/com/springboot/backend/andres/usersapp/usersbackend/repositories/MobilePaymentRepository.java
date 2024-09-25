package com.springboot.backend.andres.usersapp.usersbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.backend.andres.usersapp.usersbackend.entities.MobilePayment;

public interface MobilePaymentRepository extends JpaRepository<MobilePayment, Long>{

}
