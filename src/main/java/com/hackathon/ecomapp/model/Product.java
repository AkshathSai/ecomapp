package com.hackathon.ecomapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Double carbonFootprint; // Carbon footprint per unit

    public Product() {}

    public Product(String name, String description, BigDecimal price, Integer stock, Double carbonFootprint) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.carbonFootprint = carbonFootprint;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Double getCarbonFootprint() { return carbonFootprint; }
    public void setCarbonFootprint(Double carbonFootprint) { this.carbonFootprint = carbonFootprint; }
}
