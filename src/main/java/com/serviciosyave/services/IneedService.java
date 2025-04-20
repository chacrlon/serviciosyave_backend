package com.serviciosyave.services;  

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.serviciosyave.entities.Ineed;
import com.serviciosyave.repositories.IneedRepository;
import java.util.List;  
import org.json.JSONObject;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

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
        List<Ineed> necesidades = ineedRepository.findAll();
        HttpClient client = HttpClient.newHttpClient();

        for (Ineed necesidad : necesidades) {
            try {
                String[] coordenadas = parseCoordinates(necesidad.getUbicacion());
                double latitud = Double.parseDouble(coordenadas[0]);
                double longitud = Double.parseDouble(coordenadas[1]);
                String direccion = fetchAddressFromCoordinates(client, latitud, longitud);
                necesidad.setUbicacion(direccion);
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

    private String fetchAddressFromCoordinates(HttpClient client, double latitud, double longitud) throws Exception {
        String url = "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=" + latitud + "&lon=" + longitud;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonObject = new JSONObject(response.body());
        return jsonObject.getString("display_name");
    }

    public Ineed obtenerNecesidadPorId(Long id) {  
        return ineedRepository.findById(id).orElse(null);  
    }  

    public void eliminarNecesidad(Long id) {  
        ineedRepository.deleteById(id);  
    } 
    
}