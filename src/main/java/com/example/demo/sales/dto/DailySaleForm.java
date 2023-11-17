package com.example.demo.sales.dto;

import lombok.Getter;

@Getter
public class DailySaleForm {
    private final String storeId;
    private final Integer amount;

    public DailySaleForm(String storeId, Integer amount) {
        this.storeId = storeId;
        this.amount = amount;
    }
}
