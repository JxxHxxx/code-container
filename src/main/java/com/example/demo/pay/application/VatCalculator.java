package com.example.demo.pay.application;

public class VatCalculator {

    private final static int VAT_RATE = 11;

    // 테스트중
    public static Integer execute(Integer totalAmount) {
        return (totalAmount / VAT_RATE) + (totalAmount % VAT_RATE);
    }

}
