package com.example.demo.sales.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
@ToString
public class PayDto {

    private final String storeId;
    private final Integer payAmount;
    private final Integer vatAmount;
    private final LocalDate createdDate;
}
