package com.serviciosyave.dto;

import com.serviciosyave.Enum.NegotiationStatus;

public class NegotiationDTO {
	private Double amount;
	private String justification;
	private Long ineedId;
	private Long senderUserId;
	private Long receiverUserId;
	private NegotiationStatus negotiationStatus;
	private Long id;
	private Long sendId;

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

	public Long getIneedId() {
		return ineedId;
	}

	public void setIneedId(Long ineedId) {
		this.ineedId = ineedId;
	}

	public Long getSenderUserId() {
		return senderUserId;
	}

	public void setSenderUserId(Long senderUserId) {
		this.senderUserId = senderUserId;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSendId() {
		return sendId;
	}

	public void setSendId(Long sendId) {
		this.sendId = sendId;
	}

	public NegotiationStatus getNegotiationStatus() {
		return negotiationStatus;
	}

	public void setNegotiationStatus(NegotiationStatus negotiationStatus) {
		this.negotiationStatus = negotiationStatus;
	}

	public Long getReceiverUserId() {
		return receiverUserId;
	}

	public void setReceiverUserId(Long receiverUserId) {
		this.receiverUserId = receiverUserId;
	}
}