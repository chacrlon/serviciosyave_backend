package com.serviciosyave.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// DTO para actualización
@Data
public class PaymentUpdateRequest {
	@NotBlank
	private String reference;
}