package com.example.demo.pay.dto;

import lombok.Getter;

@Getter
public class PayResult<T> {

    private final int status;
    private final String message;
    private final T response;

    public PayResult(int status, String message, T response) {
        this.status = status;
        this.message = message;
        this.response = response;
    }
}
