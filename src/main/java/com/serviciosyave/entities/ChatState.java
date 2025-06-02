package com.serviciosyave.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ChatState {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "payment_id", unique = true)
	private Long paymentId;

	private boolean negotiationEnabled;
	private boolean negotiationCancelledByProvider;
	private boolean negotiationCancelledByClient;
	private boolean serviceApprovedByProvider;
	private boolean serviceApprovedByClient;
	private boolean serviceConfirmed;

	@Column(name = "last_updated")
	private LocalDateTime lastUpdated = LocalDateTime.now();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public boolean isNegotiationEnabled() {
		return negotiationEnabled;
	}

	public void setNegotiationEnabled(boolean negotiationEnabled) {
		this.negotiationEnabled = negotiationEnabled;
	}

	public boolean isNegotiationCancelledByProvider() {
		return negotiationCancelledByProvider;
	}

	public void setNegotiationCancelledByProvider(boolean negotiationCancelledByProvider) {
		this.negotiationCancelledByProvider = negotiationCancelledByProvider;
	}

	public boolean isNegotiationCancelledByClient() {
		return negotiationCancelledByClient;
	}

	public void setNegotiationCancelledByClient(boolean negotiationCancelledByClient) {
		this.negotiationCancelledByClient = negotiationCancelledByClient;
	}

	public boolean isServiceApprovedByProvider() {
		return serviceApprovedByProvider;
	}

	public void setServiceApprovedByProvider(boolean serviceApprovedByProvider) {
		this.serviceApprovedByProvider = serviceApprovedByProvider;
	}

	public boolean isServiceApprovedByClient() {
		return serviceApprovedByClient;
	}

	public void setServiceApprovedByClient(boolean serviceApprovedByClient) {
		this.serviceApprovedByClient = serviceApprovedByClient;
	}

	public boolean isServiceConfirmed() {
		return serviceConfirmed;
	}

	public void setServiceConfirmed(boolean serviceConfirmed) {
		this.serviceConfirmed = serviceConfirmed;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}