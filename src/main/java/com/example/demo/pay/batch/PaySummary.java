package com.example.demo.pay.batch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter // BeanPropertyRowMapper 가 사용
@NoArgsConstructor
@ToString
public class PaySummary {
        private String storeId;
        private Integer dailyTotalSales;
        private Integer dailyVatDeductedSales;
        private Integer dailyTotalTransaction;

    public PaySummary(String storeId, Integer dailyTotalSales, Integer dailyVatDeductedSales, Integer dailyTotalTransaction) {
        this.storeId = storeId;
        this.dailyTotalSales = dailyTotalSales;
        this.dailyVatDeductedSales = dailyVatDeductedSales;
        this.dailyTotalTransaction = dailyTotalTransaction;
    }
}
