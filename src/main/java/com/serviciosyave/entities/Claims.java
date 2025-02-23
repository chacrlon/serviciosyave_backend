package com.serviciosyave.entities;  

import jakarta.persistence.*;

import java.util.Date;

@Entity  
public class Claims {
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(nullable = false)
    private Long userId;

	@Column(nullable = false)
	private Long receiverId;

	@Column(nullable = false)
	private Long vendorServiceId;

	@Column(nullable = false)
	private String roomId;

	@Column(nullable = false)
	private ClaimStatus status = ClaimStatus.PENDING;

	@Lob
	@Column(name = "voucher_user", columnDefinition = "MEDIUMBLOB")
	private byte[] voucherUser;

	@Lob
	@Column(name = "voucher_receiver", columnDefinition = "MEDIUMBLOB")
	private byte[] voucherReceiver;

	@Column(name = "created_at", nullable = false, updatable = false)
	private Date createdAt = new Date();

	@Column(name = "update_at")
	private Date updateAt = new Date();

	public Claims() {}

	public Claims(Long userId, Long receiverId, String roomId, Long vendorServiceId) {
		this.userId = userId;
		this.receiverId = receiverId;
		this.roomId = roomId;
		this.vendorServiceId = vendorServiceId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getVendorServiceId() {
		return this.vendorServiceId;
	}

	public void setVendorServiceId(Long vendorServiceId) {
		this.vendorServiceId = vendorServiceId;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public byte[] getVoucherUser() {
		return voucherUser;
	}

	public void setVoucherUser(byte[] voucherUser) {
		this.voucherUser = voucherUser;
	}

	public byte[] getVoucherReceiver() {
		return voucherReceiver;
	}

	public void setVoucherReceiver(byte[] voucherReceiver) {
		this.voucherReceiver = voucherReceiver;
	}

	@Override
	public String toString() {
		return "Claims{" +
				"id=" + id +
				", userId=" + userId +
				", receiverId=" + receiverId +
				", roomId=" + roomId +
				'}';
	}

	public enum ClaimStatus {
		ACTIVE, PENDING, RESOLVED
	}
}