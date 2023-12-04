package com.example.demo.sales.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class PayDto {

    private final String storeId;
    private final Integer payAmount;
    private final Integer vatAmount;
    private final LocalDate createdDate;
}
