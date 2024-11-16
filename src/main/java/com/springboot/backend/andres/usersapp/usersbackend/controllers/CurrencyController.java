package com.springboot.backend.andres.usersapp.usersbackend.controllers;  

import javax.net.ssl.SSLContext;  
import javax.net.ssl.TrustManager;  
import javax.net.ssl.X509TrustManager;  
import javax.net.ssl.HttpsURLConnection;  

import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;  
import org.jsoup.nodes.Element;  
import org.springframework.web.bind.annotation.GetMapping;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  

@RestController  
@RequestMapping("/api/currency")  
public class CurrencyController {  

    // URL del BCV para obtener estadísticas del consumidor  
    private final String bcvUrl = "https://www.bcv.org.ve";  

    static {  
        try {  
            TrustManager[] trustAllCerts = new TrustManager[]{  
                new X509TrustManager() {  
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
                        return null;  
                    }  
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}  
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}  
                }  
            };  

            SSLContext sc = SSLContext.getInstance("SSL");  
            sc.init(null, trustAllCerts, new java.security.SecureRandom());  
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  

    @GetMapping("/dolar")  
    public String getExchangeRate() {  
        try {  
            // Conectar a la página y obtener el documento HTML  
            Document doc = Jsoup.connect(bcvUrl).get();  

            // Seleccionar el elemento que contiene el tipo de cambio del dólar  
            Element dollarElement = doc.select("#dolar .centrado strong").first();  

            // Verificar que el elemento existe y extraer el texto  
            if (dollarElement != null) {  
                String exchangeRateStr = dollarElement.text();  

                // Convertir el string a decimal  
                double exchangeRate = Double.parseDouble(exchangeRateStr.replace(",", ".")); // Cambio de coma a punto si es necesario  

                // Mostrar en consola  
                System.out.println("El tipo de cambio del dólar es: " + exchangeRate);  
                return String.valueOf(exchangeRate); // Convertir a String si necesitas así  
            } else {  
                System.out.println("El valor del dólar no fue encontrado.");  
                return "Valor del dólar no encontrado.";  
            }  
        } catch (Exception e) {  
            return "Error al obtener el tipo de cambio: " + e.getMessage();  
        }  
    }  
}