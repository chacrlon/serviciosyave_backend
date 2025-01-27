package com.serviciosyave.services;  

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Service;  
import com.serviciosyave.entities.Ubication;  
import com.serviciosyave.repositories.UbicationRepository;  
import java.util.Optional;  

@Service  
public class UbicationService {  

    @Autowired  
    private UbicationRepository ubicationRepository;

    // Método para guardar la ubicación  
    public Ubication saveUbication(Ubication ubication) {  
        return ubicationRepository.save(ubication);  
    }  

    // Método para buscar la ubicación por ID  
    public Ubication findById(Long id) {  
        Optional<Ubication> ubicacionOpt = ubicationRepository.findById(id);  
        return ubicacionOpt.orElse(null); // Retorna null si no se encuentra la ubicación  
    }  
}