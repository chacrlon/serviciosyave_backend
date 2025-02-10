package com.serviciosyave.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.serviciosyave.entities.ServiceFilter;
import com.serviciosyave.entities.User;
import com.serviciosyave.entities.UserStatus;
import com.serviciosyave.entities.VendorService;
import com.serviciosyave.repositories.UserRepository;
import com.serviciosyave.repositories.VendorServiceRepository;

@Service  
public class VendorServiceService {  

    @Autowired  
    private VendorServiceRepository vendorServiceRepository;  
    
    @Autowired  
    private UserRepository userRepository; 

    
    public List<VendorService> getAllAvailableServices() {  
        // Obtén todos los usuarios que no están ocupados  
        List<User> availableUsers = userRepository.findByStatus(UserStatus.NO_OCUPADO);  
        
        // Recopila todos los servicios de estos usuarios  
        List<VendorService> availableServices = new ArrayList<>();  
        for (User user : availableUsers) {  
            availableServices.addAll(user.getVendorServices());  
        }  
        
        return availableServices;  
    } 

    public List<VendorService> filterServices(ServiceFilter filter) {  
        // Obtén todos los usuarios que no están ocupados  
        List<User> availableUsers = userRepository.findByStatus(UserStatus.NO_OCUPADO);  
        
        // Recopila todos los servicios de estos usuarios  
        List<VendorService> availableServices = new ArrayList<>();  
        for (User user : availableUsers) {  
            availableServices.addAll(user.getVendorServices());  
        }  

        // Inicializa la lista de resultados con los servicios disponibles  
        List<VendorService> resultList = availableServices;  

        // Filtra por categoría y subcategoría  
        if (filter.getCategoryId() != null) {  
            resultList = resultList.stream()  
                .filter(service -> service.getCategory() != null && service.getCategory().getId().equals(filter.getCategoryId()))  
                .collect(Collectors.toList());  
        }  

        if (filter.getSubcategoryId() != null) {  
            resultList = resultList.stream()  
                .filter(service -> service.getSubcategory() != null && service.getSubcategory().getId().equals(filter.getSubcategoryId()))  
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
        serviceToUpdate.setCategory(serviceDetails.getCategory());  
        serviceToUpdate.setSubcategory(serviceDetails.getSubcategory());  
        serviceToUpdate.setRemoto(serviceDetails.getRemoto());  
        serviceToUpdate.setLatitude(serviceDetails.getLatitude());  
        serviceToUpdate.setLongitude(serviceDetails.getLongitude());    
        
        return vendorServiceRepository.save(serviceToUpdate);  
    }  

    public void deleteService(Long id) {  
        VendorService serviceToDelete = vendorServiceRepository.findById(id).orElseThrow();  
        vendorServiceRepository.delete(serviceToDelete);  
    }   
    
    public List<VendorService> getServicesByUserId(Long userId) {  
        return vendorServiceRepository.findByUserId(userId);  
    }  
    
    public List<VendorService> getNearbyServices(double latitude, double longitude, double distance) {  
        return vendorServiceRepository.findNearbyServices(latitude, longitude, distance);  
    }  
}