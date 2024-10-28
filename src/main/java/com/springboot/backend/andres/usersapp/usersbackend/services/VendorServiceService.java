package com.springboot.backend.andres.usersapp.usersbackend.services;  

import java.util.List;  
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Service;
import com.springboot.backend.andres.usersapp.usersbackend.entities.ServiceFilter;
import com.springboot.backend.andres.usersapp.usersbackend.entities.VendorService;  
import com.springboot.backend.andres.usersapp.usersbackend.repositories.VendorServiceRepository;  

@Service  
public class VendorServiceService {  

    @Autowired  
    private VendorServiceRepository vendorServiceRepository;
    
    public List<VendorService> filterServices(ServiceFilter filter) {  
        // Obtiene todos los servicios desde el repositorio  
        List<VendorService> resultList = vendorServiceRepository.findAll();  

        // Filtra por categoría y subcategoría  
        if (filter.getCategoria() != null && filter.getSubcategoria() != null) {  
            resultList = resultList.stream()  
                .filter(service -> service.getCategoria().equals(filter.getCategoria()) &&  
                                   service.getSubcategoria().equals(filter.getSubcategoria()))  
                .collect(Collectors.toList());  
        }  

        // Filtra por rango de precio  
        if (filter.getMinPrecio() != null || filter.getMaxPrecio() != null) {  
            resultList = resultList.stream()  
                .filter(service ->   
                    (filter.getMinPrecio() == null || service.getPrecio() >= filter.getMinPrecio()) &&  
                    (filter.getMaxPrecio() == null || service.getPrecio() <= filter.getMaxPrecio()))  
                .collect(Collectors.toList());  
        }  

        // Filtra por destacado  
        if (filter.getDestacado() != null) {  
            resultList = resultList.stream()  
                .filter(service -> filter.getDestacado().equals(service.getDestacado()))  
                .collect(Collectors.toList());  
        }  

        // Filtra por cercanía utilizando la latitud y longitud del usuario  
        if (filter.getLatitude() != null && filter.getLongitude() != null) {  
            double distance = 10.0; // Puedes definir una distancia máxima, por ejemplo, 10 km  
            List<VendorService> nearbyServices = vendorServiceRepository.findNearbyServices(  
                filter.getLatitude(),  
                filter.getLongitude(),  
                distance  
            );  

            // Filtramos los servicios que están cerca  
            resultList = resultList.stream()  
                .filter(nearbyServices::contains)  
                .collect(Collectors.toList());  
        }  

        // Filtra por remoto, texto libre, etc., puede añadirse aquí.  

        return resultList;  
    }

    public VendorService createService(VendorService service) {  
        return vendorServiceRepository.save(service);  
    }  

    public List<VendorService> getAllServices() {  
        return vendorServiceRepository.findAll();  
    }  

    public Optional<VendorService> getServiceById(Long id) {  
        return vendorServiceRepository.findById(id);  
    }  

    public VendorService updateService(Long id, VendorService serviceDetails) {  
    	
        VendorService serviceToUpdate = vendorServiceRepository.findById(id).orElseThrow();   
        
     // Actualiza los campos modificables  
        serviceToUpdate.setNombre(serviceDetails.getNombre());  
        serviceToUpdate.setDescripcion(serviceDetails.getDescripcion());  
        serviceToUpdate.setPrecio(serviceDetails.getPrecio());  
        serviceToUpdate.setDestacado(serviceDetails.getDestacado());  
        serviceToUpdate.setCategoria(serviceDetails.getCategoria());  
        serviceToUpdate.setSubcategoria(serviceDetails.getSubcategoria());  
        serviceToUpdate.setRemoto(serviceDetails.getRemoto());  
        serviceToUpdate.setLatitude(serviceDetails.getLatitude());  
        serviceToUpdate.setLongitude(serviceDetails.getLongitude());    
        
        return vendorServiceRepository.save(serviceToUpdate);  
    }  

    public void deleteService(Long id) {  
        VendorService serviceToDelete = vendorServiceRepository.findById(id).orElseThrow();  
        vendorServiceRepository.delete(serviceToDelete);  
    } 
    
 // Método para obtener todos los servicios por el ID del usuario  
    public List<VendorService> getServicesByUserId(Long userId) {  
        return vendorServiceRepository.findByUserId(userId); // Cambié a findByUserId  
    }
    
    public List<VendorService> getNearbyServices(double latitude, double longitude, double distance) {  
        return vendorServiceRepository.findNearbyServices(latitude, longitude, distance);  
    }
}