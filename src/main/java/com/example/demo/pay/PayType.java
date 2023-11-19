package com.example.demo.pay;

import lombok.Getter;

@Getter
public enum PayType {
    CASH("현금"), CARD("체크/신용카드"), EASY_PAY("간편결제");

    private final String description;

    PayType(String description) {
        this.description = description;
    }
}
