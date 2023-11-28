package com.example.demo.sales.dto;

import com.example.demo.sales.SystemType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class SalesSummaryForm {
    private final String storeId;
    private final Integer dailyTotalSales;
    private final Integer dailyVatDeductedSales;

    private final Integer dailyTotalTransaction;
    private final LocalDate salesDate;
    private final SystemType createSystem;

}
