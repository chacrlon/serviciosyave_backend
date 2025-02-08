package com.serviciosyave.services;

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Service;  
import org.springframework.web.multipart.MultipartFile;

import com.serviciosyave.entities.Seller;
import com.serviciosyave.repositories.SellerRepository;

import java.io.File;  
import java.io.IOException;  
import java.nio.file.Paths;
import java.sql.Date;
import java.util.UUID;  

@Service  
public class SellerService {  

    @Autowired  
    private SellerRepository sellerRepository;  

    private final String UPLOAD_DIR = "C:\\Users\\USUARIO\\Pictures\\servicip"; // Define la ruta donde se guardarán los archivos  

    public Seller registerSeller(Seller seller, MultipartFile file) throws IOException {  
        if (file != null && !file.isEmpty()) {  
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();  
            File destinationFile = new File(Paths.get(UPLOAD_DIR, filename).toString());  
            file.transferTo(destinationFile);  
            seller.setProfilePicture(filename);  
        }  
        seller.setCreatedAt(new Date(0));  
        return sellerRepository.save(seller);  
    }  
}