package com.example.capstone1.Service;

import com.example.capstone1.Model.MerchantStock;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Model.User;
import com.example.capstone1.Model.Wishlist;
import com.example.capstone1.Repository.ProductRepository;
import com.example.capstone1.Repository.UserRepository;
import com.example.capstone1.Repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserService {

    private final ProductRepository productRepository;
    private final MerchantStockService merchantStockService;
    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;



    ArrayList<Product> shoppingCart=new ArrayList<>();

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public boolean updateUser(Integer id,User user) {
        User oldUser=userRepository.getById(id);

        if (oldUser==null) {
            return false;
        }

        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(user.getPassword());
        oldUser.setEmail(user.getEmail());
        oldUser.setBalance(user.getBalance());
        oldUser.setRole(user.getRole());
        userRepository.save(oldUser);
        return true;
    }

    public boolean deleteUser(Integer id) {
        User oldUser=userRepository.getById(id);
        if (oldUser==null) {
            return false;
        }
        userRepository.delete(oldUser);
        return true;
    }

    public int buyProduct(String userId, String productId, String merchantId) {

        User user = users.stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
        if (user == null) return 4;

        Product product = productService.products.stream().filter(p -> p.getId().equals(productId)).findFirst().orElse(null);
        if (product == null) return 3;

        MerchantStock merchantStock = merchantStockService.merchantStocks.stream().filter(ms -> ms.getMerchantID().equals(merchantId) && ms.getProductID().equals(productId)).findFirst().orElse(null);
        if (merchantStock == null) return 2;

        if (merchantStock.getStock() <= 0) return 1;

        if (user.getBalance() < product.getPrice()) return 5;

        user.setBalance(user.getBalance() - product.getPrice());
        merchantStock.setStock(merchantStock.getStock() - 1);
        user.getPurchasedProducts().add(productId);
        return 0;
    }


    //Extra 1

    public List<Wishlist> getWishlist(Integer userID) {
        return wishlistRepository.findAll();
    }

    public Boolean addWishlist(Integer userId ,Wishlist wishlist) {
        if (userRepository.getById(userId) == null) return false;
            wishlistRepository.save(wishlist);
            return true;
    }

    public Boolean deleteWishlist(Integer id) {
        if (wishlistRepository.getById(id)==null) return false;
        wishlistRepository.delete(wishlistRepository.getById(id));
        return true;
    }

    public int moveToShoppingCart(String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                if (user.getWishlist() == null) {
                    user.setWishlist(new ArrayList<>());
                }
                if (!user.getWishlist().isEmpty()) {
                    shoppingCart.addAll(user.getWishlist());
                    user.getWishlist().clear();
                    return 0;
                }
                return 1;
            }
        }
        return 2;
    }


    //Extra 2

    public ArrayList<Product> getShoppingCart() {
        return shoppingCart;
    }

    public int addShoppingCart(String productId, String merchantId) {
                for (MerchantStock merchantStock : merchantStockService.merchantStocks) {
                    if (merchantStock.getMerchantID().equals(merchantId)) {
                        for (Product product : productService.products) {
                            if (product.getId().equals(productId)) {
                                    shoppingCart.add(product);
                                    return 0;
                            }
                        }
                        return 1;
                    }
                }
                return 2;

    }

    public boolean deleteShoppingCart(String id) {
        for (Product product : shoppingCart) {
            if (product.getId().equals(id)) {
                shoppingCart.remove(product);
                return true;
            }
        }
        return false;
    }

    public int buyAllInShoppingCart(String userId) {

        User user = users.stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
        if (user == null) return 4;

        if (shoppingCart.isEmpty()) return 6;

        for (Product product : shoppingCart) {
            boolean purchased = false;

            for (MerchantStock merchantStock : merchantStockService.merchantStocks) {
                if (merchantStock.getProductID().equals(product.getId())) {
                    int result = buyProduct(userId, product.getId(), merchantStock.getMerchantID());
                    if (result == 0) {
                        purchased = true;
                        break;
                    } else {
                        return result;
                    }
                }
            }

            if (!purchased) return 3;
        }
        shoppingCart.clear();
        return 0;
    }

    //Extra 4

    public List<Product> getPurchaseHistory(String userId) {

        User user = users.stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
        if (user == null) return null;

        List<Product> purchasedProducts = new ArrayList<>();

        for (String productId : user.getPurchasedProducts()) {
            Product product = productService.products.stream().filter(p -> p.getId().equals(productId)).findFirst().orElse(null);
            if (product != null) purchasedProducts.add(product);
        }

        return purchasedProducts;
    }

    //Extra 5

    public List<Product> recommendProducts(String userId) {
        User user = users.stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
        if (user == null) return null;

        Map<String, Integer> categoryCount = new HashMap<>();

        for (String productId : user.getPurchasedProducts()) {
            Product product = productService.products.stream().filter(p -> p.getId().equals(productId)).findFirst().orElse(null);
            if (product != null) {
                String categoryId = product.getCategoryId();
                categoryCount.put(categoryId, categoryCount.getOrDefault(categoryId, 0) + 1);
            }
        }

        String mostPurchasedCategory = categoryCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (mostPurchasedCategory == null) return new ArrayList<>();

        List<Product> recommendedProducts = productService.products.stream()
                .filter(p -> p.getCategoryId().equals(mostPurchasedCategory))
                .filter(p -> !user.getPurchasedProducts().contains(p.getId()))
                .filter(p -> p.getPrice() <= user.getBalance())
                .toList();

        return recommendedProducts;
    }




}
