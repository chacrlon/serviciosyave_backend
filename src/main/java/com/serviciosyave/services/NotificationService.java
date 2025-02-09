package com.serviciosyave.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.serviciosyave.entities.Notification;
import com.serviciosyave.repositories.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;

@Service  
public class NotificationService {  

    @Autowired  
    private NotificationRepository notificationRepository;  
    
    public Notification approveServiceByProvider(Long notificationId) {  
        Notification notificationToUpdate = notificationRepository.findById(notificationId)  
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));  
        // Actualizar el resultado del proveedor  
        notificationToUpdate.setResultadoProveedor("aprobado");  
        return notificationRepository.save(notificationToUpdate);  
    }  

    public Notification approveServiceByClient(Long notificationId) {  
        Notification notificationToUpdate = notificationRepository.findById(notificationId)  
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));  
        // Actualizar el resultado del consumidor  
        notificationToUpdate.setResultadoConsumidor("aprobado");
     // Actualizar el estatus a "finalizado"  
        notificationToUpdate.setEstatus("finalizado");
        return notificationRepository.save(notificationToUpdate);  
    }  
    
    
    
    public Notification rejectServiceByProvider(Long notificationId) {  
        Notification notificationToUpdate = notificationRepository.findById(notificationId)  
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));  
        // Actualizar el resultado del proveedor a "rechazado"  
        notificationToUpdate.setResultadoProveedor("rechazado"); 
     // Actualizar el estatus a "finalizado"  
        notificationToUpdate.setEstatus("finalizado");
        return notificationRepository.save(notificationToUpdate);  
    }   

    public Notification rejectServiceByClient(Long notificationId) {  
        Notification notificationToUpdate = notificationRepository.findById(notificationId)  
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));  
        // Actualizar el resultado del consumidor a "rechazado"  
        notificationToUpdate.setResultadoConsumidor("rechazado");
     // Actualizar el estatus a "finalizado"  
        notificationToUpdate.setEstatus("finalizado");
        return notificationRepository.save(notificationToUpdate);  
    }  

    
}