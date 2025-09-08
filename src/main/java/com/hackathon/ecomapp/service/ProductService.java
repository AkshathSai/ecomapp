package com.hackathon.ecomapp.service;

import com.hackathon.ecomapp.exception.ResourceNotFoundException;
import com.hackathon.ecomapp.exception.InsufficientStockException;
import com.hackathon.ecomapp.model.Product;
import com.hackathon.ecomapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public Product getProductByIdRequired(String id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public boolean checkStockAvailability(String productId, Integer quantity) {
        Optional<Product> product = productRepository.findById(productId);
        return product.isPresent() && product.get().getStock() >= quantity;
    }

    public void updateStock(String productId, Integer quantity) {
        Product product = getProductByIdRequired(productId);
        if (product.getStock() < quantity) {
            throw new InsufficientStockException(product.getName(), quantity, product.getStock());
        }
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }
}
