package com.example.capstone1.Service;

import com.example.capstone1.Model.Merchant;
import com.example.capstone1.Repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantService {
    private final MerchantRepository merchantRepository;

    public List<Merchant> getMerchants() {
        return merchantRepository.findAll();
    }

    public void addMerchant(Merchant merchant) {
        merchantRepository.save(merchant);
    }

    public boolean updateMerchant(Integer id,Merchant merchant) {
        Merchant oldMerchant = merchantRepository.getById(id);

        if (oldMerchant != null) {
            oldMerchant.setName(merchant.getName());
            merchantRepository.save(oldMerchant);
            return true;
        }
        return false;
    }

    public boolean deleteMerchant(Integer id) {
        Merchant merchant = merchantRepository.getById(id);
        if (merchant != null) {
            merchantRepository.delete(merchant);
            return true;
        }
        return false;
    }

}
