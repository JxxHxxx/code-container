package com.example.demo.pay.domain;

import lombok.Getter;

@Getter
public enum PayStatus {

    REQUEST_PAYMENT("결제 요청"),
    PAYMENT_PROGRESS("결제 진행 중"),
    PAYMENT_COMPLETED("결제 완료"),
    REFUND_COMPLETED("환불 완료");

    private final String description;

    PayStatus(String description) {
        this.description = description;
    }
}
