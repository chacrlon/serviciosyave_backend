package com.serviciosyave.models;  

import jakarta.validation.constraints.NotBlank;  

public class VerificationRequest {  

    @NotBlank(message = "El código de verificación no puede estar vacío")  
    private String verificationCode;  

    // Getter y Setter  
    public String getVerificationCode() {  
        return verificationCode;  
    }  

    public void setVerificationCode(String verificationCode) {  
        this.verificationCode = verificationCode;  
    }  
}