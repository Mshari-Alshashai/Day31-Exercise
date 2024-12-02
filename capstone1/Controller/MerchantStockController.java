package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.MerchantStock;
import com.example.capstone1.Service.MerchantStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchant-stock")
@RequiredArgsConstructor
public class MerchantStockController {
    private final MerchantStockService merchantStockService;

    @GetMapping("/get")
    public ResponseEntity getMerchantStock() {
        return ResponseEntity.status(200).body(merchantStockService.getMerchantStocks());
    }

    @PostMapping("/add")
    public ResponseEntity addMerchantStock(@RequestBody @Valid MerchantStock merchantStock, Errors errors) {
        if (errors.hasErrors()) return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        return switch (merchantStockService.addMerchantStock(merchantStock)){
            case 0 -> ResponseEntity.status(200).body(new ApiResponse("Merchant Stock Added Successfully"));
            case 1 -> ResponseEntity.status(400).body(new ApiResponse("Merchant Not Found"));
            case 2 -> ResponseEntity.status(400).body(new ApiResponse("Product Not Found"));
            default -> ResponseEntity.status(400).body(new ApiResponse("Merchant Stock Failed To Be Added"));
        };
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateMerchantStock(@PathVariable Integer id,@RequestBody @Valid MerchantStock merchantStock,Errors errors) {
        if (errors.hasErrors()) return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        if (merchantStockService.updateMerchantStock(id,merchantStock)) return ResponseEntity.status(200).body(new ApiResponse("Merchant stock updated successfully"));
        return ResponseEntity.status(400).body(new ApiResponse("Merchant stock not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteMerchantStock(@PathVariable Integer id) {
        if (merchantStockService.deleteMerchantStock(id)) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant stock deleted successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Merchant stock not found"));
    }

    @PutMapping("/add-stock/{id}/{quantity}")
    public ResponseEntity addStock(@PathVariable Integer id, @PathVariable Integer quantity) {
        if (merchantStockService.addStock(id,quantity)){
           return ResponseEntity.status(200).body(new ApiResponse("Stock added successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Stock not found"));
    }

    @GetMapping("/filter-products/{categoryId}/{minPrice}/{maxPrice}/{inStockOnly}")
    public ResponseEntity filterProducts(@PathVariable String categoryId,@PathVariable Double minPrice,@PathVariable Double maxPrice, @PathVariable boolean inStockOnly){
        return ResponseEntity.status(200).body(merchantStockService.filterProducts(categoryId, minPrice, maxPrice, inStockOnly));
    }

    @GetMapping("/sort-products/{sortBy}/{page}/{size}")
    public ResponseEntity sortProducts(@PathVariable String sortBy,@PathVariable int page,@PathVariable int size){
        return ResponseEntity.status(200).body(merchantStockService.sortProducts(sortBy, page, size));
    }


}
