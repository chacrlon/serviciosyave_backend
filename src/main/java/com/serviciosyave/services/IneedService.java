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

    @Autowired
    private GPSService gpsService;

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

    public Ineed crearNecesidad(Ineed ineed) throws Exception {
        String address = gpsService.fetchAddressFromCoordinates(ineed.getLatitude(), ineed.getLongitude());
        ineed.setAddress(address);
        return ineedRepository.save(ineed);
    }

    public List<Ineed> obtenerNecesidades(Double customerLat, Double customerLong) {
        List<Ineed> necesidades = ineedRepository.findAll();

        for (Ineed necesidad : necesidades) {
            try {
                double latitudIneed = necesidad.getLatitude();
                double longitudIneed = necesidad.getLongitude();
                double nearby = gpsService.nearbyDistance(latitudIneed, longitudIneed, customerLat, customerLong);
                necesidad.setNearby(nearby);
            } catch (Exception e) {
                System.err.println("Error processing necesidad with ID " + necesidad.getId() + ": " + e.getMessage());
            }
        }
        return necesidades;
    }

    private String[] parseCoordinates(String ubicacion) {
        String[] coordenadas = ubicacion.split(", ");
        return new String[] {
            coordenadas[0].split(": ")[1],
            coordenadas[1].split(": ")[1]
        };
    }

    public Ineed obtenerNecesidadPorId(Long id) {  
        return ineedRepository.findById(id).orElse(null);  
    }  

    public void eliminarNecesidad(Long id) {  
        ineedRepository.deleteById(id);  
    } 
    
}