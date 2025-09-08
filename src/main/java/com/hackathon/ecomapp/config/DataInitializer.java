package com.hackathon.ecomapp.config;

import com.hackathon.ecomapp.model.Product;
import com.hackathon.ecomapp.model.Promotion;
import com.hackathon.ecomapp.repository.ProductRepository;
import com.hackathon.ecomapp.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    final ProductRepository productRepository;
    final PromotionRepository promotionRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize sample products if none exist
        if (productRepository.count() == 0) {
            initializeProducts();
        }

        // Initialize sample promotions if none exist
        if (promotionRepository.count() == 0) {
            initializePromotions();
        }
    }

    private void initializeProducts() {
        // Create sample products with carbon footprint data
        Product laptop = new Product("Gaming Laptop", "High-performance gaming laptop",
                                    new BigDecimal("1200.00"), 10, 45.5);
        Product phone = new Product("Smartphone", "Latest smartphone with advanced features",
                                   new BigDecimal("800.00"), 25, 12.3);
        Product headphones = new Product("Wireless Headphones", "Noise-cancelling wireless headphones",
                                        new BigDecimal("200.00"), 50, 5.2);
        Product tablet = new Product("Tablet", "10-inch tablet for productivity",
                                    new BigDecimal("400.00"), 15, 18.7);
        Product watch = new Product("Smart Watch", "Fitness tracking smartwatch",
                                   new BigDecimal("300.00"), 30, 8.9);

        productRepository.save(laptop);
        productRepository.save(phone);
        productRepository.save(headphones);
        productRepository.save(tablet);
        productRepository.save(watch);

        System.out.println("Sample products initialized");
    }

    private void initializePromotions() {
        // Create sample promotion codes
        Promotion discount10 = new Promotion("SAVE10", "10% off on orders over $100",
                                           Promotion.DiscountType.PERCENTAGE, new BigDecimal("10"),
                                           new BigDecimal("100"), LocalDateTime.now(),
                                           LocalDateTime.now().plusDays(30));
        discount10.setUsageLimit(100);

        Promotion discount50 = new Promotion("SAVE50", "$50 off on orders over $500",
                                           Promotion.DiscountType.FIXED_AMOUNT, new BigDecimal("50"),
                                           new BigDecimal("500"), LocalDateTime.now(),
                                           LocalDateTime.now().plusDays(30));
        discount50.setUsageLimit(50);

        Promotion welcome = new Promotion("WELCOME20", "20% off for new customers",
                                        Promotion.DiscountType.PERCENTAGE, new BigDecimal("20"),
                                        new BigDecimal("50"), LocalDateTime.now(),
                                        LocalDateTime.now().plusDays(60));
        welcome.setUsageLimit(200);

        promotionRepository.save(discount10);
        promotionRepository.save(discount50);
        promotionRepository.save(welcome);

        System.out.println("Sample promotions initialized");
    }
}
