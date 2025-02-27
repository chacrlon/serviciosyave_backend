package com.serviciosyave.dto;

public class NegotiationDTO {
	private Double amount;
	private String justification;
	private Long ineedId;
	private Long senderUserId;
	private Long receiverUserId;

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

	public Long getReceiverUserId() {
		return receiverUserId;
	}

	public void setReceiverUserId(Long receiverUserId) {
		this.receiverUserId = receiverUserId;
	}
}