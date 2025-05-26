package com.serviciosyave.services;

import com.serviciosyave.models.OSMResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LocationService {

    private final RestTemplate restTemplate;

    @Autowired
    public LocationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getServiceArea(double latitude, double longitude) {
        String url = "https://nominatim.openstreetmap.org/reverse?format=json&lat="
                + latitude + "&lon=" + longitude + "&accept-language=es";

        try {
            OSMResponse response = restTemplate.getForObject(url, OSMResponse.class);

            if (response == null || response.getAddress() == null) {
                return "Ubicación no determinada";
            }

            return extractLocation(response);
        } catch (Exception e) {
            return "Error al obtener ubicación";
        }
    }

    private String extractLocation(OSMResponse response) {
        String city = response.getAddress().getCity();
        String state = response.getAddress().getState();

        // Fallbacks para Venezuela
        if (city == null) city = response.getAddress().getTown(); // Ej: Pueblo Llano, Mérida
        if (city == null) city = response.getAddress().getVillage(); // Ej: La Azulita
        if (state == null) state = response.getAddress().getRegion(); // Ej: Región Los Andes

        return (city != null ? city : "Ciudad no detectada") + ", "
                + (state != null ? state : "Estado no detectado");
    }
}