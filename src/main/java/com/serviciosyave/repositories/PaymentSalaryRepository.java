package com.serviciosyave.repositories;

import com.serviciosyave.Enum.PaymentStatus;
import com.serviciosyave.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentSalaryRepository extends JpaRepository<PaymentSalary, Long> {
    List<PaymentSalary> findByUser(User user);
    List<PaymentSalary> findByPaymentDateBetween(LocalDateTime start, LocalDateTime end);
    List<PaymentSalary> findByStatus(PaymentStatus status);

    public interface BinanceRepository extends JpaRepository<Binance, Long> {
        Binance findByUser(User user);
    }

    public interface BankTransferRepository extends JpaRepository<BankTransfer, Long> {
        BankTransfer findByUser(User user);
    }

    public interface MobilePaymentRepository extends JpaRepository<MobilePayment, Long> {
        MobilePayment findByUser(User user);
    }
}