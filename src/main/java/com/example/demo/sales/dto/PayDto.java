package com.example.demo.sales.dto;

import com.example.demo.sales.SalesSummary;
import com.example.demo.sales.SystemType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class PayDto {

    private final String storeId;
    private final Integer totalAmount;
    private final Integer vatAmount;
    private final LocalDate createdDate;
}
