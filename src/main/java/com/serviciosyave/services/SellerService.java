package com.serviciosyave.services;  

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Service;
import com.serviciosyave.entities.Seller;
import com.serviciosyave.repositories.SellerRepository;  

@Service  
public class SellerService {  

    @Autowired  
    private SellerRepository sellerRepository;  

    public Seller saveSeller(Seller seller) {  
        return sellerRepository.save(seller);  
    }  

    public Seller updateSeller(Long id, Seller seller) {  
        seller.setId(id);  
        return sellerRepository.save(seller);  
    }
}
    