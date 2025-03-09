package com.serviciosyave.services;  

import com.serviciosyave.entities.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;  
import com.serviciosyave.controllers.NotificationController;
import com.serviciosyave.repositories.NotificationRepository;
import com.serviciosyave.repositories.PaymentRepository;  
import com.serviciosyave.repositories.UserRepository;  
import com.serviciosyave.repositories.VendorServiceRepository;  
import java.util.ArrayList;  
import java.util.List;  

@Service  
public class PaymentService {  

    @Autowired  
    private PaymentRepository paymentRepository;  

    @Autowired  
    private UserRepository userRepository;

    @Autowired
    private VendorServiceRepository vendorServiceRepository;

    @Autowired
    private IneedService ineedService;

    @Autowired  
    private NotificationRepository notificationRepository; 

    @Autowired  
    private EmailService emailService;  

    @Autowired  
    private NotificationController notificationController;  

    public Payment createdPayment(Payment payment) {  
        return paymentRepository.save(payment);  
    }  
    
    public void approvePayment(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        Long ineedId = payment.getIneedId();

        payment.setEstatus("aprobado");
        paymentRepository.save(payment);
        Long vendorServiceId = payment.getVendorServiceId();


        if(ineedId != null) {
            Ineed ineedService = this.ineedService.findById(payment.getIneedId());
            Long buyerId = ineedService.getUserId();

            User seller = userRepository.findById(payment.getUsersId())
                    .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));
            User buyer = userRepository.findById(buyerId)
                    .orElseThrow(() -> new RuntimeException("Comprador no encontrado"));

            String messageToSeller = "Has comprado el servicio " + ineedService.getTitulo() + " de " + buyer.getUsername() + ".";
            String messageToBuyer = "El usuario " + seller.getUsername() + " ha comprado tu servicio " + ineedService.getTitulo() + ".";
            emailService.sendEmail(seller.getEmail(), "Notificación de Servicio", messageToSeller);
            emailService.sendEmail(buyer.getEmail(), "Confirmación de Compra", messageToBuyer);

            // Crear notificaciones y capturar los IDs
            Long sellerNotificationId = notificationController.notifyUser(seller.getId(), buyer.getId(), messageToSeller, "Seller", null, payment.getIneedId());
            Long buyerNotificationId = notificationController.notifyUser(buyer.getId(), seller.getId(), messageToBuyer, "Buyer", null, payment.getIneedId());

            // Aquí puedes almacenar los IDs en la notificación si es necesario
            // Por ejemplo, si tienes una lógica para almacenar los IDs en algún lado
            // Puedes crear una lógica adicional para actualizar las notificaciones con el id2
            if (sellerNotificationId != null && buyerNotificationId != null) {
                // Actualizar la notificación del vendedor con el ID de la notificación del comprador
                Notification sellerNotification = notificationRepository.findById(sellerNotificationId).orElse(null);
                if (sellerNotification != null) {
                    sellerNotification.setId2(buyerNotificationId);
                    notificationRepository.save(sellerNotification);
                }

                // Actualizar la notificación del comprador con el ID de la notificación del vendedor
                Notification buyerNotification = notificationRepository.findById(buyerNotificationId).orElse(null);
                if (buyerNotification != null) {
                    buyerNotification.setId2(sellerNotificationId);
                    notificationRepository.save(buyerNotification);
                }
            }

        } else {
            VendorService vendorService = vendorServiceRepository.findById(vendorServiceId)
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
            Long sellerId = vendorService.getUserId();

            User seller = userRepository.findById(sellerId)
                    .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));
            User buyer = userRepository.findById(payment.getUsersId())
                    .orElseThrow(() -> new RuntimeException("Comprador no encontrado"));

            String messageToSeller = "El usuario " + buyer.getUsername() + " ha comprado tu servicio " + vendorService.getNombre() + ".";
            String messageToBuyer = "Has comprado el servicio " + vendorService.getNombre() + " de " + seller.getUsername() + ".";

            emailService.sendEmail(seller.getEmail(), "Notificación de Servicio", messageToSeller);
            emailService.sendEmail(buyer.getEmail(), "Confirmación de Compra", messageToBuyer);

            // Crear notificaciones y capturar los IDs
            Long sellerNotificationId = notificationController.notifyUser(seller.getId(), buyer.getId(), messageToSeller, "Seller", vendorServiceId, null);
            Long buyerNotificationId = notificationController.notifyUser(buyer.getId(), seller.getId(), messageToBuyer, "Buyer", vendorServiceId, null);

            // Aquí puedes almacenar los IDs en la notificación si es necesario
            // Por ejemplo, si tienes una lógica para almacenar los IDs en algún lado
            // Puedes crear una lógica adicional para actualizar las notificaciones con el id2
            if (sellerNotificationId != null && buyerNotificationId != null) {
                // Actualizar la notificación del vendedor con el ID de la notificación del comprador
                Notification sellerNotification = notificationRepository.findById(sellerNotificationId).orElse(null);
                if (sellerNotification != null) {
                    sellerNotification.setId2(buyerNotificationId);
                    notificationRepository.save(sellerNotification);
                }

                // Actualizar la notificación del comprador con el ID de la notificación del vendedor
                Notification buyerNotification = notificationRepository.findById(buyerNotificationId).orElse(null);
                if (buyerNotification != null) {
                    buyerNotification.setId2(sellerNotificationId);
                    notificationRepository.save(buyerNotification);
                }
            }

        }

    }
    
    
    public List<PaymentDTO> getAllPayments() {  
        List<Payment> payments = paymentRepository.findAll();  
        List<PaymentDTO> paymentDTOs = new ArrayList<>();  

        for (Payment payment : payments) {  
            User user = userRepository.findById(payment.getUsersId()).orElse(null);
            PaymentDTO paymentDTO = new PaymentDTO();  
            paymentDTO.setId(payment.getId());  
            paymentDTO.setMonto(payment.getMonto());  
            paymentDTO.setDivisa(payment.getDivisa());  
            paymentDTO.setMetodoPago(payment.getMetodoPago());  
            paymentDTO.setReferencia(payment.getReferencia());  
            paymentDTO.setEstatus(payment.getEstatus());  
            paymentDTO.setUsername(user != null ? user.getUsername() : "Desconocido");  

            if (payment instanceof mobilePaymentPayment) {  
                mobilePaymentPayment mobilePayment = (mobilePaymentPayment) payment;  
                paymentDTO.setTelefono(mobilePayment.getTelefono());  // Si PaymentDTO tiene este campo  
            } else if (payment instanceof bankTransferPayment) {  
                bankTransferPayment bankTransferPayment = (bankTransferPayment) payment;  
                paymentDTO.setNumeroCuenta(bankTransferPayment.getNumeroCuenta());  // Idem  
            } else if (payment instanceof BinancePayment) {  
                BinancePayment binancePayment = (BinancePayment) payment;  
                paymentDTO.setEmailBinance(binancePayment.getEmailBinance());  // Idem  
            }  

            paymentDTOs.add(paymentDTO);  
        }  

        return paymentDTOs;  
    }  

    public List<Payment> findByVendorServiceIdAndUsersId(Long vendorServiceId, Long usersId) {
        List<Payment> payment = paymentRepository.findByVendorServiceIdAndUsersId(vendorServiceId, usersId);
        return payment;
    }

    public void rejectPayment(Long paymentId) {  
        Payment payment = paymentRepository.findById(paymentId).orElse(null);  
        if (payment != null) {  
            payment.setEstatus("rechazado");  
            paymentRepository.save(payment);  
        }  
    }  
}