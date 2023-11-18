package com.example.demo.sales;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
public class SalesSummary {

    @Id @GeneratedValue
    private Long id;

    private String storeId;
    private Integer dailyTotalSales;
    private Integer dailyVatDeductedSales;
    private LocalDate createDate;
    private String createSystem;

    public SalesSummary(String storeId, Integer dailyTotalSales, Integer dailyVatDeductedSales, String createSystem) {
        this.storeId = storeId;
        this.dailyTotalSales = dailyTotalSales;
        this.dailyVatDeductedSales = dailyVatDeductedSales;
        this.createSystem = createSystem;
        this.createDate = LocalDate.now();
    }
}
