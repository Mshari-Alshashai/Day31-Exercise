package com.example.capstone1.Service;

import com.example.capstone1.Model.Merchant;
import com.example.capstone1.Model.MerchantStock;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Repository.MerchantRepository;
import com.example.capstone1.Repository.MerchantStockRepository;
import com.example.capstone1.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantStockService {

    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantStockRepository merchantStockRepository;

    public List<MerchantStock> getMerchantStocks() {
        return merchantStockRepository.findAll();
    }

    public Integer addMerchantStock(MerchantStock merchantStock) {
        if (productRepository.existsById(merchantStock.getProductID())){
            if (merchantRepository.existsById(merchantStock.getMerchantID())){
                merchantStockRepository.save(merchantStock);
                return 0;
            }
            return 1;
        }
        return 2;
    }

    public Boolean updateMerchantStock(Integer id, MerchantStock merchantStock) {
        MerchantStock old = merchantStockRepository.getById(id);
        if (old == null){
            return false;
        }
        if (merchantRepository.findById(old.getMerchantID()).isEmpty()){
            return false;
        }
        if (productRepository.findById(old.getProductID()).isEmpty()){
            return false;
        }

        old.setProductID(merchantStock.getProductID());
        old.setMerchantID(merchantStock.getMerchantID());
        old.setStock(merchantStock.getStock());
        merchantStockRepository.save(old);
        return true;
    }

    public Boolean deleteMerchantStock(Integer id) {
        MerchantStock old = merchantStockRepository.getById(id);
        if (old == null){
            return false;
        }
        merchantStockRepository.delete(old);
        return true;
    }

    public Boolean addStock(Integer id,Integer quantity) {
        if (merchantStockRepository.getById(id) == null){
            return false;
        }
        merchantStockRepository.getById(id).setStock(merchantStockRepository.getById(id).getStock()+quantity);
        return true;
    }


    //Extra 3
    public List<Product> filterProducts(String categoryId, Double minPrice, Double maxPrice, boolean inStockOnly) {
        List<Product> filteredProducts = productService.products.stream()
                .filter(product -> product.getCategoryId().equals(categoryId))
                .filter(product -> (minPrice == null || product.getPrice() >= minPrice))
                .filter(product -> (maxPrice == null || product.getPrice() <= maxPrice))
                .collect(Collectors.toList());

        if (inStockOnly) {
            filteredProducts.removeIf(product -> {
                int totalStock = merchantStocks.stream()
                        .filter(stock -> stock.getProductID().equals(product.getId()))
                        .mapToInt(MerchantStock::getStock)
                        .sum();
                return totalStock == 0;
            });
        }

        return filteredProducts;
    }

    public List<Product> sortProducts(String sortBy, int page, int size) {
        List<Product> sortedProducts = new ArrayList<>(productService.products);

        if ("price".equalsIgnoreCase(sortBy)) {
            sortedProducts.sort(Comparator.comparingDouble(Product::getPrice));
        } else if ("name".equalsIgnoreCase(sortBy)) {
            sortedProducts.sort(Comparator.comparing(Product::getName));
        }

        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, sortedProducts.size());

        if (startIndex >= sortedProducts.size()) {
            return Collections.emptyList();
        }

        return sortedProducts.subList(startIndex, endIndex);
    }

}
