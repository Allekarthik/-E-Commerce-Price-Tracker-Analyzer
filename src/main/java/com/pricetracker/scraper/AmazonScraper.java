package com.pricetracker.scraper;

import com.pricetracker.model.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AmazonScraper extends BaseScraper {

    @Override
    public Product scrapeProduct(String url) {
        Product product = null;

        try {
            System.out.println("Scraping Amazon: " + url);
            driver.get(url);

            // Wait for page to load
            Thread.sleep(2000);

            // Extract product name
            String productName = "N/A";
            try {
                WebElement nameElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.id("productTitle")));
                productName = nameElement.getText().trim();
            } catch (Exception e) {
                System.out.println("Could not find product name");
            }

            // Extract price
            double price = 0.0;
            String priceText = "";
            try {
                // Try multiple price selectors
                try {
                    WebElement priceElement = driver.findElement(By.cssSelector(".a-price-whole"));
                    priceText = priceElement.getText().replace(",", "").replace("₹", "").trim();
                } catch (Exception e1) {
                    try {
                        WebElement priceElement = driver.findElement(By.id("priceblock_ourprice"));
                        priceText = priceElement.getText().replace(",", "").replace("₹", "").trim();
                    } catch (Exception e2) {
                        WebElement priceElement = driver.findElement(By.id("priceblock_dealprice"));
                        priceText = priceElement.getText().replace(",", "").replace("₹", "").trim();
                    }
                }
                price = Double.parseDouble(priceText);
            } catch (Exception e) {
                System.out.println("Could not find price");
                price = 0.0;
            }

            // Check availability
            String availability = "Available";
            try {
                WebElement availElement = driver.findElement(By.id("availability"));
                String availText = availElement.getText().toLowerCase();
                if (availText.contains("out of stock") || availText.contains("unavailable")) {
                    availability = "Out of Stock";
                }
            } catch (Exception e) {
                availability = "Unknown";
            }

            product = new Product(productName, url, "Amazon", price, availability);
            System.out.println("✓ Scraped: " + product);

        } catch (Exception e) {
            System.err.println("Error scraping Amazon: " + e.getMessage());
        }

        return product;
    }
}