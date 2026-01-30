package com.pricetracker.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Product {
    private String name;
    private String url;
    private String website;
    private double price;
    private String availability;
    private String timestamp;

    public Product(String name, String url, String website, double price, String availability) {
        this.name = name;
        this.url = url;
        this.website = website;
        this.price = price;
        this.availability = availability;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Getters
    public String getName() { return name; }
    public String getUrl() { return url; }
    public String getWebsite() { return website; }
    public double getPrice() { return price; }
    public String getAvailability() { return availability; }
    public String getTimestamp() { return timestamp; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setAvailability(String availability) { this.availability = availability; }

    @Override
    public String toString() {
        return String.format("Product: %s | Website: %s | Price: ₹%.2f | Status: %s | Time: %s",
                name, website, price, availability, timestamp);
    }
}