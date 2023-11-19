package com.example.demo.pay.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PayForm {
    private Integer amount;
    public PayForm(Integer amount) {
        this.amount = amount;
    }
}
