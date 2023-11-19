package com.example.demo.sales.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SalesSummaryForm {
    private final String storeId;
    private final Integer dailyTotalSales;
    private final Integer dailyVatDeductedSales;
    private final LocalDate salesDate;
    private final String createSystem;

    public SalesSummaryForm(String storeId, Integer dailyTotalSales, Integer dailyVatDeductedSales, LocalDate salesDate, String createSystem) {
        this.storeId = storeId;
        this.dailyTotalSales = dailyTotalSales;
        this.dailyVatDeductedSales = dailyVatDeductedSales;
        this.salesDate = salesDate;
        this.createSystem = createSystem;
    }
}
