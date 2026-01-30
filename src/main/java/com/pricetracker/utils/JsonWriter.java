package com.pricetracker.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pricetracker.model.Product;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JsonWriter {

    public static void writeToJson(List<Product> products, String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(products, writer);
            System.out.println("✓ JSON report saved: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing JSON file: " + e.getMessage());
        }
    }
}