package com.example.demo.pay.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PayRequest {
    private Integer amount;
    public PayRequest(Integer amount) {
        this.amount = amount;
    }
}
