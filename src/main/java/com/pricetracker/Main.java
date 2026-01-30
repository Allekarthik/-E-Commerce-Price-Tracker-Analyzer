package com.pricetracker;

import com.pricetracker.model.Product;
import com.pricetracker.scraper.AmazonScraper;
import com.pricetracker.utils.ExcelWriter;
import com.pricetracker.utils.JsonWriter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   E-COMMERCE PRICE TRACKER STARTED");
        System.out.println("===========================================\n");

        // Product URLs to track - Multiple Amazon products
        Map<String, String> productUrls = new HashMap<>();

        // Add multiple products to track
        productUrls.put("iPhone_15_Blue", "https://www.amazon.in/Apple-iPhone-15-128-GB/dp/B0CHX2F5QT");
        productUrls.put("Samsung_S24", "https://www.amazon.in/Apple-iPhone-15-128-GB/dp/B0CHX1W1XY");



        // You can add more products easily:
        // productUrls.put("Product_Name", "AMAZON_URL");

        List<Product> allProducts = new ArrayList<>();

        // Create one scraper instance and reuse it
        AmazonScraper scraper = new AmazonScraper();

        int successCount = 0;
        int failCount = 0;

        // Scrape all products
        for (Map.Entry<String, String> entry : productUrls.entrySet()) {
            try {
                System.out.println("\n[" + (successCount + failCount + 1) + "/" + productUrls.size() + "] Processing: " + entry.getKey());
                Product product = scraper.scrapeProduct(entry.getValue());

                if (product != null && product.getPrice() > 0) {
                    allProducts.add(product);
                    successCount++;
                } else {
                    System.out.println("⚠ Failed to scrape: " + entry.getKey());
                    failCount++;
                }

                // Add delay between requests to avoid being blocked
                Thread.sleep(2000);

            } catch (Exception e) {
                System.err.println("✗ Error processing " + entry.getKey() + ": " + e.getMessage());
                failCount++;
            }
        }

        scraper.closeBrowser();

        // Display results
        System.out.println("\n===========================================");
        System.out.println("   SCRAPING COMPLETED");
        System.out.println("===========================================");
        System.out.println("✓ Successfully scraped: " + successCount);
        System.out.println("✗ Failed to scrape: " + failCount);
        System.out.println("Total products: " + allProducts.size());

        if (!allProducts.isEmpty()) {
            // Find lowest and highest prices
            Product lowestPriceProduct = allProducts.stream()
                    .filter(p -> p.getPrice() > 0)
                    .min((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()))
                    .orElse(null);

            Product highestPriceProduct = allProducts.stream()
                    .filter(p -> p.getPrice() > 0)
                    .max((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()))
                    .orElse(null);

            System.out.println("\n" + "=".repeat(50));
            System.out.println("   PRICE ANALYSIS");
            System.out.println("=".repeat(50));

            if (lowestPriceProduct != null) {
                System.out.println("\n💰 LOWEST PRICE:");
                System.out.println("   " + lowestPriceProduct.getName());
                System.out.println("   Price: ₹" + String.format("%,.2f", lowestPriceProduct.getPrice()));
            }

            if (highestPriceProduct != null) {
                System.out.println("\n💎 HIGHEST PRICE:");
                System.out.println("   " + highestPriceProduct.getName());
                System.out.println("   Price: ₹" + String.format("%,.2f", highestPriceProduct.getPrice()));
            }

            // Calculate average price
            double avgPrice = allProducts.stream()
                    .filter(p -> p.getPrice() > 0)
                    .mapToDouble(Product::getPrice)
                    .average()
                    .orElse(0.0);

            System.out.println("\n📊 AVERAGE PRICE: ₹" + String.format("%,.2f", avgPrice));

            // Show all products
            System.out.println("\n" + "=".repeat(50));
            System.out.println("   ALL PRODUCTS");
            System.out.println("=".repeat(50));
            for (int i = 0; i < allProducts.size(); i++) {
                Product p = allProducts.get(i);
                System.out.println("\n" + (i + 1) + ". " + p.getName());
                System.out.println("   Price: ₹" + String.format("%,.2f", p.getPrice()));
                System.out.println("   Status: " + p.getAvailability());}

            // Generate timestamp for filenames
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            // Save to Excel
            String excelFilename = "PriceTracker_" + timestamp + ".xlsx";
            ExcelWriter.writeToExcel(allProducts, excelFilename);

            // Save to JSON
            String jsonFilename = "PriceTracker_" + timestamp + ".json";
            JsonWriter.writeToJson(allProducts, jsonFilename);

            System.out.println("\n" + "=".repeat(50));
            System.out.println("   REPORTS GENERATED");
            System.out.println("=".repeat(50));
            System.out.println("📄 Excel: " + excelFilename);
            System.out.println("📄 JSON: " + jsonFilename);
        }

        System.out.println("\n===========================================");
        System.out.println("   PROGRAM COMPLETED SUCCESSFULLY");
        System.out.println("===========================================");
    }
}