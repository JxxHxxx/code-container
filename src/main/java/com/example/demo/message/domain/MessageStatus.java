package com.example.demo.message.domain;

import lombok.Getter;

@Getter
public enum MessageStatus {
    SENT(10,"요청 완료"),
    RETRY(20,"재처리"),
    SUCCESS(30,"처리 완료"),
    FAIL(40,"처리 실패");

    private final int code;
    private final String description;

    MessageStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
