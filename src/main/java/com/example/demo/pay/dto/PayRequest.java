package com.example.demo.pay.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class PayRequest {
    private Integer amount;

    public PayRequest() {
    }
    public PayRequest(Integer amount) {
        this.amount = amount;
    }
}
