package com.example.demo.sales.dto;

import lombok.Getter;

@Getter
public class SalesSummaryForm {
    private final String storeId;
    private final Integer dailyTotalSales;
    private final Integer dailyVatDeductedSales;
    private final String createSystem;

    public SalesSummaryForm(String storeId, Integer dailyTotalSales, Integer dailyVatDeductedSales, String createSystem) {
        this.storeId = storeId;
        this.dailyTotalSales = dailyTotalSales;
        this.dailyVatDeductedSales = dailyVatDeductedSales;
        this.createSystem = createSystem;
    }
}
