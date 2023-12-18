package com.example.demo.pay.support;

import com.example.demo.pay.domain.PayStatus;

import java.util.Random;

public class PayStatusProvider {

    public PayStatus provide() {
        int nextInt = new Random().nextInt(6);

        return decidePayStatus(nextInt);
    }

    private static PayStatus decidePayStatus(int nextInt) {
        switch(nextInt) {
            case 0:
                return PayStatus.REFUND_COMPLETED;
            default:
                return PayStatus.PAYMENT_COMPLETED;
        }
    }
}
