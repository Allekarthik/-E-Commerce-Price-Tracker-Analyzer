package com.pricetracker.scraper;

import com.pricetracker.model.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class FlipkartScraper extends BaseScraper {

    @Override
    public Product scrapeProduct(String url) {
        Product product = null;

        try {
            System.out.println("Scraping Flipkart: " + url);
            driver.get(url);

            // Wait for page load
            Thread.sleep(3000);

            // Close login popup if it appears
            try {
                WebElement closeButton = driver.findElement(By.cssSelector("button._2KpZ6l"));
                closeButton.click();
                Thread.sleep(1000);
            } catch (Exception e) {
                // Popup didn't appear, continue
            }

            // Extract product name - Updated selectors
            String productName = "N/A";
            try {
                // Try multiple selectors for product name
                try {
                    WebElement nameElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("span.VU-ZEz")));
                    productName = nameElement.getText().trim();
                } catch (Exception e1) {
                    try {
                        WebElement nameElement = driver.findElement(By.cssSelector("h1.yhB1nd"));
                        productName = nameElement.getText().trim();
                    } catch (Exception e2) {
                        try {
                            WebElement nameElement = driver.findElement(By.cssSelector("span.B_NuCI"));
                            productName = nameElement.getText().trim();
                        } catch (Exception e3) {
                            System.out.println("Could not find product name with any selector");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Could not find product name");
            }

            // Extract price - Updated selectors
            double price = 0.0;
            try {
                // Try multiple price selectors
                try {
                    WebElement priceElement = driver.findElement(By.cssSelector("div.Nx9bqj"));
                    String priceText = priceElement.getText()
                            .replace("₹", "")
                            .replace(",", "")
                            .trim();
                    price = Double.parseDouble(priceText);
                } catch (Exception e1) {
                    try {
                        WebElement priceElement = driver.findElement(By.cssSelector("div._30jeq3"));
                        String priceText = priceElement.getText()
                                .replace("₹", "")
                                .replace(",", "")
                                .trim();
                        price = Double.parseDouble(priceText);
                    } catch (Exception e2) {
                        try {
                            WebElement priceElement = driver.findElement(By.cssSelector("div._16Jk6d"));
                            String priceText = priceElement.getText()
                                    .replace("₹", "")
                                    .replace(",", "")
                                    .trim();
                            price = Double.parseDouble(priceText);
                        } catch (Exception e3) {
                            System.out.println("Could not find price with any selector");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Could not find price");
            }

            // Check availability
            String availability = "Available";
            try {
                String pageSource = driver.getPageSource().toLowerCase();
                if (pageSource.contains("out of stock") ||
                        pageSource.contains("sold out") ||
                        pageSource.contains("currently unavailable")) {
                    availability = "Out of Stock";
                }
            } catch (Exception e) {
                availability = "Unknown";
            }

            product = new Product(productName, url, "Flipkart", price, availability);
            System.out.println("✓ Scraped: " + product);

        } catch (Exception e) {
            System.err.println("Error scraping Flipkart: " + e.getMessage());
        }

        return product;
    }
}