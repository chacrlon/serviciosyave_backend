package com.serviciosyave.repositories;

import com.serviciosyave.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import com.serviciosyave.entities.Payment;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

    public Payment findByVendorServiceId(Long vendorServiceId);

    public List<Payment> findByVendorServiceIdAndUsersId(Long vendorServiceId, Long usersId);

    public List<Payment> findByVendorServiceIdOrIneedIdAndUsersIdAndReceiverId(Long vendorServiceId, Long ineedId,Long usersId, Long receiverId);

}