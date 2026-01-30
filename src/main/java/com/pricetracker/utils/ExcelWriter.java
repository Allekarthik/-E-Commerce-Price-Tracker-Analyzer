package com.pricetracker.utils;

import com.pricetracker.model.Product;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelWriter {

    public static void writeToExcel(List<Product> products, String filename) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Price Tracker Results");

        // Create header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Product Name", "Website", "Price (₹)", "Availability", "Timestamp", "URL"};

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Fill data
        int rowNum = 1;
        for (Product product : products) {
            if (product != null) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(product.getName());
                row.createCell(1).setCellValue(product.getWebsite());
                row.createCell(2).setCellValue(product.getPrice());
                row.createCell(3).setCellValue(product.getAvailability());
                row.createCell(4).setCellValue(product.getTimestamp());
                row.createCell(5).setCellValue(product.getUrl());
            }
        }

        // Auto-size columns
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to file
        try (FileOutputStream fileOut = new FileOutputStream(filename)) {
            workbook.write(fileOut);
            System.out.println("\n✓ Excel report saved: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing Excel file: " + e.getMessage());
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}