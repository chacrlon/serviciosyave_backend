package com.serviciosyave.Enum;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaymentUpdateRequest {
    @NotBlank
    private String reference;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}