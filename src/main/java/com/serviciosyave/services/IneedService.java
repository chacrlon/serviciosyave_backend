package com.serviciosyave.services;  

import com.serviciosyave.entities.Category;
import com.serviciosyave.entities.Subcategory;
import com.serviciosyave.repositories.CategoryRepository;
import com.serviciosyave.repositories.SubcategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
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
    private CategoryRepository categoryRepository; // Nuevo repositorio añadido

    @Autowired
    private SubcategoryRepository subcategoryRepository; // Opcional si usas subcategorías

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
        // Validar que la categoría tenga formulario
        Category categoriaCompleta = categoryRepository.findById(ineed.getCategory().getId())
                .orElseThrow(() -> new ValidationException("Categoría no encontrada"));

        // Validar formulario
        if(categoriaCompleta.getFormulario() == null) {
            throw new ValidationException("La categoría no tiene formulario configurado");
        }

        // Asignar la categoría completa al Ineed
        ineed.setCategory(categoriaCompleta);

        // Opcional: Hacer lo mismo para subcategoría si es necesario
        if(ineed.getSubcategory() != null) {
            Subcategory subcategoriaCompleta = subcategoryRepository.findById(ineed.getSubcategory().getId())
                    .orElseThrow(() -> new ValidationException("Subcategoría no encontrada"));
            ineed.setSubcategory(subcategoriaCompleta);
        }

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