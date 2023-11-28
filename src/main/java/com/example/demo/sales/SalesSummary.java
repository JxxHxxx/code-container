package com.example.demo.sales;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private Integer dailyTotalTransaction;
    private LocalDate salesDate;
    private LocalDateTime createTime;

    @Enumerated(EnumType.STRING)
    private SystemType createSystem;

    public SalesSummary(String storeId, Integer dailyTotalSales, Integer dailyVatDeductedSales, Integer dailyTotalTransaction,
                        LocalDate salesDate, SystemType createSystem) {
        this.storeId = storeId;
        this.dailyTotalSales = dailyTotalSales;
        this.dailyVatDeductedSales = dailyVatDeductedSales;
        this.dailyTotalTransaction = dailyTotalTransaction;
        this.salesDate = salesDate;
        this.createTime = LocalDateTime.now();
        this.createSystem = createSystem;
    }

    public void update(Integer totalAmount, Integer vatDeductedAmount) {
       this.dailyTotalSales += totalAmount;
       this.dailyVatDeductedSales += vatDeductedAmount;
       this.dailyTotalTransaction += 1;
    }
}
