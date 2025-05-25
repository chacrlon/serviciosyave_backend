package com.serviciosyave.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serviciosyave.Enum.PaymentStatus;
import com.serviciosyave.Enum.PaymentUpdateRequest;
import com.serviciosyave.entities.*;
import com.serviciosyave.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentSalaryService {

    private static final Logger log = LoggerFactory.getLogger(PaymentSalaryService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentSalaryRepository paymentSalaryRepository;

    @Autowired
    private BinanceRepository binanceRepository;

    @Autowired
    private BankTransferRepository bankTransferRepository;

    @Autowired
    private MobilePaymentRepository mobilePaymentRepository;


    // Obtener pagos pendientes
    public List<PaymentSalary> getPendingPayments() {
        return paymentSalaryRepository.findByStatus(PaymentStatus.PENDIENTE);
    }

    // Actualizar pago individual
    @Transactional
    public PaymentSalary updatePayment(Long paymentId, PaymentUpdateRequest request) {
        PaymentSalary payment = paymentSalaryRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        if (payment.getStatus() == PaymentStatus.PAGADO) {
            throw new RuntimeException("El pago ya fue procesado");
        }

        payment.setReference(request.getReference());
        payment.setStatus(PaymentStatus.PAGADO);
        payment.setPaymentDate(LocalDateTime.now());

        return paymentSalaryRepository.save(payment);
    }


    // Generar pagos pendientes (ejecutar manualmente o por cron)
    @Transactional
    public void generatePendingPayments() {
        List<User> users = userRepository.findByUserMoneyGreaterThan(BigDecimal.ZERO);

        users.forEach(user -> {
            PaymentMethodSelected method = user.getPaymentMethodSelected();
            if (method == null) {
                log.error("Usuario {} sin método de pago configurado", user.getEmail());
                return; // Saltar usuarios sin método
            }

            Object paymentDetails = getPaymentDetailsForUser(user, method);
            if (paymentDetails == null) {
                log.error("Usuario {} sin datos de pago asociados", user.getEmail());
                return; // Saltar usuarios sin detalles
            }

            PaymentSalary payment = new PaymentSalary();
            payment.setUser(user);
            payment.setAmount(user.getUserMoney());
            payment.setPaymentMethod(method);
            payment.setStatus(PaymentStatus.PENDIENTE); // Estado inicial
            payment.setPaymentDate(null); // Fecha se llenará al pagar
            payment.setReference(null);
            payment.setCedula(user.getCedula());
            payment.setPhone(user.getPhone());
            payment.setEmail(user.getEmail());
            payment.setPaymentDetails(serializePaymentDetails(paymentDetails));

            paymentSalaryRepository.save(payment);

            user.setUserMoney(BigDecimal.ZERO);
            userRepository.save(user);
        });
    }





    private Object getPaymentDetailsForUser(User user, PaymentMethodSelected method) {
        switch (method) {
            case BINANCE:
                Binance binance = binanceRepository.findByUser(user).orElse(null); // Cambio aquí
                return (binance != null) ? Map.of(
                        "email", binance.getBinanceEmail()
                ) : null;
            case TRANSFERENCIA:
                BankTransfer transfer = bankTransferRepository.findByUser(user).orElse(null); // Cambio aquí
                return (transfer != null) ? Map.of(
                        "cuentaBancaria", transfer.getCuentaBancaria(),
                        "nombreTitular", transfer.getNombreTitular(),
                        "rif", transfer.getRif()
                ) : null;
            case PAGO_MOVIL:
                MobilePayment mobile = mobilePaymentRepository.findByUser(user).orElse(null); // Cambio aquí
                return (mobile != null) ? Map.of(
                        "cedula", mobile.getCedula(),
                        "telefono", mobile.getNumeroTelefono(),
                        "banco", mobile.getBanco()
                ) : null;
            default:
                return null;
        }
    }

    public List<PaymentSalary> getPaymentHistory() {
        return paymentSalaryRepository.findAll(); // Obtiene todos los registros de pagos
    }

    // Si necesitas filtrar por usuario, también puedes agregar:
    public List<PaymentSalary> getPaymentHistoryByUser(User user) {
        return paymentSalaryRepository.findByUser(user);
    }

    private String serializePaymentDetails(Object details) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(details);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}