package com.example.demo.pay.domain;

public class ImpossibleRefundException extends RuntimeException {
    public ImpossibleRefundException(String message) {
        super(message);
    }
}
