package com.pricetracker.scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public abstract class BaseScraper {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BaseScraper() {
        // Set the path to manually downloaded EdgeDriver
        System.setProperty("webdriver.edge.driver", "C:\\edgedriver\\msedgedriver.exe");

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");

        // Uncomment below to run without showing browser window
        // options.addArguments("--headless");

        driver = new EdgeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        System.out.println("✓ Edge browser started successfully!");
    }

    public abstract com.pricetracker.model.Product scrapeProduct(String url);

    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}