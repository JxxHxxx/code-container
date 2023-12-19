package com.example.demo.message.dto;

import lombok.Getter;

@Getter
public enum OrderMessageType {

    REFUND("환불 요청");

    private final String description;

    OrderMessageType(String description) {
        this.description = description;
    }
}
