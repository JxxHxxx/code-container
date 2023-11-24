package com.example.demo.message.domain;

import lombok.Getter;

@Getter
public enum ReceiverType {
    PAYMENT("결제 서버"), STORE("가게");

    private final String description;

    ReceiverType(String description) {
        this.description = description;
    }
}
