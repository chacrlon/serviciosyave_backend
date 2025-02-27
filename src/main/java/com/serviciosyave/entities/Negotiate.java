package com.serviciosyave.entities;

import com.serviciosyave.Enum.NegotiationStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Negotiate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double amount;
	private String justification;
	private LocalDateTime timestamp;

	@ManyToOne
	@JoinColumn(name = "ineed_id")
	private Ineed ineed;

	@ManyToOne
	@JoinColumn(name = "sender_id")
	private User sender;

	@ManyToOne
	@JoinColumn(name = "receiver_id")
	private User receiver;

	private String threadId; // Nombre consistente con el repositorio

	private int offerCount;

	@Enumerated(EnumType.STRING)
	private NegotiationStatus status;

	@PrePersist
	public void generateThreadId() {
		this.threadId = String.format("%d-%d-%d",
				ineed.getId(),
				Math.min(sender.getId(), receiver.getId()),
				Math.max(sender.getId(), receiver.getId()));
	}

	public Negotiate() {

	}

	public Negotiate(Long id, Double amount, String justification, LocalDateTime timestamp,
					 Ineed ineed, User sender, User receiver, String threadId, int offerCount,
					 NegotiationStatus status) {
		this.id = id;
		this.amount = amount;
		this.justification = justification;
		this.timestamp = timestamp;
		this.ineed = ineed;
		this.sender = sender;
		this.receiver = receiver;
		this.threadId = threadId;
		this.offerCount = offerCount;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Ineed getIneed() {
		return ineed;
	}

	public void setIneed(Ineed ineed) {
		this.ineed = ineed;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	public int getOfferCount() {
		return offerCount;
	}

	public void setOfferCount(int offerCount) {
		this.offerCount = offerCount;
	}

	public NegotiationStatus getStatus() {
		return status;
	}

	public void setStatus(NegotiationStatus status) {
		this.status = status;
	}
}