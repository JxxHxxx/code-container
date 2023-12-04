package com.example.demo.pay.domain;

public enum PayStatus {
    DEPOSIT("입금 완료"),
    COMPLETED("거래 종료"),
    REFUNDED("환불");

    private final String description;

    PayStatus(String description) {
        this.description = description;
    }
}
