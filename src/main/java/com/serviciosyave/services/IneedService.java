package com.serviciosyave.services;  

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.serviciosyave.entities.Ineed;
import com.serviciosyave.repositories.IneedRepository;
import java.util.List;  

@Service  
public class IneedService {  

	@Autowired 
	private final IneedRepository ineedRepository;

    public Ineed findById(Long id) {
        return ineedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ineed no encontrado con ID: " + id));
    }

    public Ineed getIneedById(Long id) {
        return ineedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ineed no encontrado con ID: " + id));
    }
 
    public IneedService(IneedRepository ineedRepository) {  
        this.ineedRepository = ineedRepository;  
    }  

    public Ineed crearNecesidad(Ineed ineed) {  
        return ineedRepository.save(ineed);  
    }  

    public List<Ineed> obtenerNecesidades() {  
        return ineedRepository.findAll();  
    }  

    public Ineed obtenerNecesidadPorId(Long id) {  
        return ineedRepository.findById(id).orElse(null);  
    }  

    public void eliminarNecesidad(Long id) {  
        ineedRepository.deleteById(id);  
    } 
    
}