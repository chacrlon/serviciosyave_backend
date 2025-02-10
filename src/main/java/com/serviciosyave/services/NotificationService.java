package com.serviciosyave.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.serviciosyave.entities.Notification;
import com.serviciosyave.repositories.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;

@Service  
public class NotificationService {  

    @Autowired  
    private NotificationRepository notificationRepository;  
    
    public List<Notification> approveServiceByProvider(Long notificationId, Long notificationId2) {  
        Notification notificationToUpdate1 = notificationRepository.findById(notificationId)  
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + notificationId));  
        Notification notificationToUpdate2 = notificationRepository.findById(notificationId2)  
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + notificationId2));  
        
        // Actualizar el resultado del proveedor  
        notificationToUpdate1.setResultadoProveedor("aprobado");  
        notificationToUpdate2.setResultadoProveedor("aprobado");  
        
        return notificationRepository.saveAll(List.of(notificationToUpdate1, notificationToUpdate2));  
    }  

    public List<Notification> approveServiceByClient(Long notificationId, Long notificationId2) {  
        Notification notificationToUpdate1 = notificationRepository.findById(notificationId)  
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + notificationId));  
        Notification notificationToUpdate2 = notificationRepository.findById(notificationId2)  
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + notificationId2));  
        
        // Actualizar el resultado del consumidor  
        notificationToUpdate1.setResultadoConsumidor("aprobado");  
        notificationToUpdate1.setEstatus("finalizado"); // También debes asegurarte de actualizar el estatus  
        
        notificationToUpdate2.setResultadoConsumidor("aprobado");  
        notificationToUpdate2.setEstatus("finalizado"); // También debes asegurarte de actualizar el estatus  
        
        return notificationRepository.saveAll(List.of(notificationToUpdate1, notificationToUpdate2));  
    }  

    public List<Notification> rejectServiceByProvider(Long notificationId, Long notificationId2) {  
        Notification notificationToUpdate1 = notificationRepository.findById(notificationId)  
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + notificationId));  
        Notification notificationToUpdate2 = notificationRepository.findById(notificationId2)  
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + notificationId2));  
        
        // Actualizar el resultado del proveedor a "rechazado"  
        notificationToUpdate1.setResultadoProveedor("rechazado");   
        notificationToUpdate1.setEstatus("finalizado");  
        
        notificationToUpdate2.setResultadoProveedor("rechazado");   
        notificationToUpdate2.setEstatus("finalizado");  
        
        return notificationRepository.saveAll(List.of(notificationToUpdate1, notificationToUpdate2));  
    }   

    public List<Notification> rejectServiceByClient(Long notificationId, Long notificationId2) {  
        Notification notificationToUpdate1 = notificationRepository.findById(notificationId)  
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + notificationId));  
        Notification notificationToUpdate2 = notificationRepository.findById(notificationId2)  
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + notificationId2));  
        
        // Actualizar el resultado del consumidor a "rechazado"  
        notificationToUpdate1.setResultadoConsumidor("rechazado");  
        notificationToUpdate1.setEstatus("finalizado");  
        
        notificationToUpdate2.setResultadoConsumidor("rechazado");  
        notificationToUpdate2.setEstatus("finalizado");  
        
        return notificationRepository.saveAll(List.of(notificationToUpdate1, notificationToUpdate2));  
    }

    
}