package com.example.demo.pay.support;

import com.example.demo.pay.domain.PayType;

import java.util.Random;

public class PayTypeProvider {

    public static PayType provide() {
        int nextInt = new Random().nextInt(6);

        return decidePayType(nextInt);
    }

    private static PayType decidePayType(int nextInt) {
        switch(nextInt) {
            case 0:
                return PayType.CASH;
            case 1:
                return PayType.EASY_PAY;
            default:
                return PayType.CARD;
        }
    }
}
