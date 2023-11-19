package com.example.demo.sales;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class SalesSummary {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storeId;
    private Integer dailyTotalSales;
    private Integer dailyVatDeductedSales;
    private LocalDate salesDate;
    private LocalDateTime createTime;
    private String createSystem;

    public SalesSummary(String storeId, Integer dailyTotalSales, LocalDate salesDate, Integer dailyVatDeductedSales, String createSystem) {
        this.storeId = storeId;
        this.dailyTotalSales = dailyTotalSales;
        this.dailyVatDeductedSales = dailyVatDeductedSales;
        this.salesDate = salesDate;
        this.createSystem = createSystem;
        this.createTime = LocalDateTime.now();
    }
}
