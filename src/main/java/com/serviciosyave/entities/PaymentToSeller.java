package com.serviciosyave.entities;  

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;  

@Entity  
public class PaymentToSeller {  

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne
	    private User seller;

	    @ManyToOne
	    private User buyer;

	    @ManyToOne
	    private VendorService vendorService;

	    private BigDecimal amount;
	    private LocalDateTime transactionDate;
	    private String status; // PENDIENTE, COMPLETADO, RECHAZADO

	    // Relaciones con los métodos de pago
	    @ManyToOne
	    private Binance binancePayment;

	    @ManyToOne
	    private BankTransfer bankTransfer;

	    @ManyToOne
	    private MobilePayment mobilePayment;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public User getSeller() {
			return seller;
		}

		public void setSeller(User seller) {
			this.seller = seller;
		}

		public User getBuyer() {
			return buyer;
		}

		public void setBuyer(User buyer) {
			this.buyer = buyer;
		}

		public VendorService getVendorService() {
			return vendorService;
		}

		public void setVendorService(VendorService vendorService) {
			this.vendorService = vendorService;
		}

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		public LocalDateTime getTransactionDate() {
			return transactionDate;
		}

		public void setTransactionDate(LocalDateTime transactionDate) {
			this.transactionDate = transactionDate;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public Binance getBinancePayment() {
			return binancePayment;
		}

		public void setBinancePayment(Binance binancePayment) {
			this.binancePayment = binancePayment;
		}

		public BankTransfer getBankTransfer() {
			return bankTransfer;
		}

		public void setBankTransfer(BankTransfer bankTransfer) {
			this.bankTransfer = bankTransfer;
		}

		public MobilePayment getMobilePayment() {
			return mobilePayment;
		}

		public void setMobilePayment(MobilePayment mobilePayment) {
			this.mobilePayment = mobilePayment;
		}
	    
	    


}