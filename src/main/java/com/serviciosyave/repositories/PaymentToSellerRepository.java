package com.serviciosyave.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.serviciosyave.entities.PaymentToSeller;

public interface PaymentToSellerRepository extends JpaRepository<PaymentToSeller, Long>{
	
	@Query("SELECT pt FROM PaymentToSeller pt " +
	           "LEFT JOIN FETCH pt.binancePayment " +
	           "LEFT JOIN FETCH pt.bankTransfer " +
	           "LEFT JOIN FETCH pt.mobilePayment")
	    List<PaymentToSeller> findAllWithDetails();
}