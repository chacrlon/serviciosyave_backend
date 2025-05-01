package com.serviciosyave.services;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service  
public class GPSService {

    public String fetchAddressFromCoordinates(double latitud, double longitud) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        String url = "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=" + latitud + "&lon=" + longitud;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonObject = new JSONObject(response.body());
        return jsonObject.getString("display_name");
    }

    public double nearbyDistance(double latitud1, double longitud1, double latitud2, double longitud2) {
        final int RADIO_TIERRA = 6371;

        double latitud1Rad = Math.toRadians(latitud1);
        double longitud1Rad = Math.toRadians(longitud1);
        double latitud2Rad = Math.toRadians(latitud2);
        double longitud2Rad = Math.toRadians(longitud2);

        double diferenciaLatitud = latitud2Rad - latitud1Rad;
        double diferenciaLongitud = longitud2Rad - longitud1Rad;

        double a = Math.sin(diferenciaLatitud / 2) * Math.sin(diferenciaLatitud / 2) +
                Math.cos(latitud1Rad) * Math.cos(latitud2Rad) *
                        Math.sin(diferenciaLongitud / 2) * Math.sin(diferenciaLongitud / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RADIO_TIERRA * c;
    }

}