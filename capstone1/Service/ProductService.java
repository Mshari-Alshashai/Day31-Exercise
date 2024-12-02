package com.example.capstone1.Service;

import com.example.capstone1.Model.Category;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Repository.CategoryRepository;
import com.example.capstone1.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Boolean addProduct(Product product) {
        if (categoryRepository.findById(product.getCategoryId()).isPresent()){
            productRepository.save(product);
            return true;
        }
        return false;
    }

    public boolean updateProduct(Integer id, Product product) {
        Product oldProduct = productRepository.getById(id);
        if (oldProduct == null){
            return false;
        }
        if (categoryRepository.findById(product.getCategoryId()).isEmpty()){
            return false;
        }
        oldProduct.setName(product.getName());
        oldProduct.setPrice(product.getPrice());
        oldProduct.setCategoryId(product.getCategoryId());
        productRepository.save(oldProduct);
        return true;
    }

    public boolean deleteProduct(Integer id) {
        Product product = productRepository.getById(id);
        if (product == null){
            return false;
        }
        productRepository.delete(product);
        return true;
    }


}
