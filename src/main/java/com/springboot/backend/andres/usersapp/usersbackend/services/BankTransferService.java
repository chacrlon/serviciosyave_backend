package com.springboot.backend.andres.usersapp.usersbackend.services;  

import com.springboot.backend.andres.usersapp.usersbackend.entities.BankTransfer;  
import com.springboot.backend.andres.usersapp.usersbackend.repositories.BankTransferRepository;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Service;  
import java.util.List;  
import java.util.Optional;  

@Service  
public class BankTransferService {  

    @Autowired  
    private BankTransferRepository bankTransferRepository;  

    public BankTransfer createBankTransfer(BankTransfer bankTransfer) {  
        return bankTransferRepository.save(bankTransfer);  
    }  

    public List<BankTransfer> getAllBankTransfers() {  
        return bankTransferRepository.findAll();  
    }  

    public Optional<BankTransfer> getBankTransferById(Long id) {  
        return bankTransferRepository.findById(id);  
    }  

    public BankTransfer updateBankTransfer(Long id, BankTransfer bankTransferDetails) {  
        BankTransfer bankTransfer = bankTransferRepository.findById(id).orElseThrow();  
        bankTransfer.setCuentaBancaria(bankTransferDetails.getCuentaBancaria());  
        bankTransfer.setNombreTitular(bankTransferDetails.getNombreTitular());  
        bankTransfer.setRif(bankTransferDetails.getRif());  
        return bankTransferRepository.save(bankTransfer);  
    }  

    public void deleteBankTransfer(Long id) {  
        bankTransferRepository.deleteById(id);  
    }  
}