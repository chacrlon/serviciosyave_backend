package com.serviciosyave.services;

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Service;  
import org.springframework.web.multipart.MultipartFile;

import com.serviciosyave.entities.Seller;
import com.serviciosyave.entities.VendorService;
import com.serviciosyave.repositories.SellerRepository;

import java.io.File;  
import java.io.IOException;  
import java.nio.file.Paths;
import java.sql.Date;
import java.util.Optional;
import java.util.UUID;  

@Service  
public class SellerService {  

    @Autowired  
    private SellerRepository sellerRepository;  

public Seller saveSeller(Seller seller) {
	return sellerRepository.save(seller);
	
}

}
