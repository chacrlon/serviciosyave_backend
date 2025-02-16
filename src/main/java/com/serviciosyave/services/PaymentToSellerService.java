package com.serviciosyave.services;  

import org.springframework.stereotype.Service;  
import org.springframework.beans.factory.annotation.Autowired;  
import com.serviciosyave.entities.Binance;
import com.serviciosyave.entities.MobilePayment;
import com.serviciosyave.entities.Notification;
import com.serviciosyave.entities.PaymentToSeller;
import com.serviciosyave.entities.User;  
import com.serviciosyave.entities.VendorService;
import com.serviciosyave.entities.BankTransfer;
import com.serviciosyave.repositories.BankTransferRepository;
import com.serviciosyave.repositories.BinanceRepository;
import com.serviciosyave.repositories.MobilePaymentRepository;
import com.serviciosyave.repositories.NotificationRepository;
import com.serviciosyave.repositories.PaymentToSellerRepository;
import com.serviciosyave.repositories.UserRepository;  
import com.serviciosyave.repositories.VendorServiceRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;  


@Service  
public class PaymentToSellerService {  

    @Autowired
    private PaymentToSellerRepository paymentToSellerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorServiceRepository vendorServiceRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private BinanceRepository binanceRepository;
    
    @Autowired
    private BankTransferRepository bankTransferRepository;
    
    @Autowired
    private MobilePaymentRepository mobilePaymentRepository;

    @Transactional
    public void processPayment(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        VendorService service = vendorServiceRepository.findById(notification.getVendorServiceId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));

        User seller = userRepository.findById(notification.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Seller not found"));

        User buyer = userRepository.findById(notification.getUserId2())
                .orElseThrow(() -> new EntityNotFoundException("Buyer not found"));

        // Obtener métodos de pago del vendedor (usando Optional correctamente)
        Optional<Binance> binanceAccount = binanceRepository.findByUser(seller);
        Optional<BankTransfer> bankAccount = bankTransferRepository.findByUser(seller);
        Optional<MobilePayment> mobilePayment = mobilePaymentRepository.findByUser(seller);

        // Crear transacción
        PaymentToSeller transaction = new PaymentToSeller();
        transaction.setSeller(seller);
        transaction.setBuyer(buyer);
        transaction.setVendorService(service);
        transaction.setAmount(BigDecimal.valueOf(service.getPrecio())); 
        transaction.setTransactionDate(LocalDateTime.now());
        
        // Asignar primer método de pago disponible
        if (binanceAccount.isPresent()) {
            transaction.setBinancePayment(binanceAccount.get());
        } else if (bankAccount.isPresent()) {
            transaction.setBankTransfer(bankAccount.get());
        } else if (mobilePayment.isPresent()) {
            transaction.setMobilePayment(mobilePayment.get());
        } else {
            throw new IllegalStateException("El vendedor no tiene métodos de pago registrados");
        }

        transaction.setStatus("COMPLETADO");
        paymentToSellerRepository.save(transaction);

        // Actualizar saldo del vendedor
        BigDecimal servicePrice = BigDecimal.valueOf(service.getPrecio()); 
        seller.setUserMoney(seller.getUserMoney().add(servicePrice));
        userRepository.save(seller);
    }
}