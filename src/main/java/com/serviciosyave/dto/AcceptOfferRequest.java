package com.serviciosyave.dto;   

public class AcceptOfferRequest {  
    private Long necesidadId;  
    private Long professionalUserId;  

    public AcceptOfferRequest() {   
    }  

    public AcceptOfferRequest(Long necesidadId, Long professionalUserId) {  
        this.necesidadId = necesidadId;  
        this.professionalUserId = professionalUserId;  
    }  

    public Long getNecesidadId() {  
        return necesidadId;  
    }  

    public void setNecesidadId(Long necesidadId) {  
        this.necesidadId = necesidadId;  
    }  

    public Long getProfessionalUserId() {  
        return professionalUserId;  
    }  

    public void setProfessionalUserId(Long professionalUserId) {  
        this.professionalUserId = professionalUserId;  
    }  
}