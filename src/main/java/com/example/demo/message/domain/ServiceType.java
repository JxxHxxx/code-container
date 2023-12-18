package com.example.demo.message.domain;

import lombok.Getter;

@Getter
public enum ServiceType {
    ORDER("주문 서버"), OTHER("제3자");

    private final String description;

    ServiceType(String description) {
        this.description = description;
    }
}
