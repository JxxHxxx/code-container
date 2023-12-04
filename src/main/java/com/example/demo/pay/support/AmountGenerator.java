package com.example.demo.pay.support;

import java.util.Random;

public class AmountGenerator {

    private static Random random = new Random();

    public static int execute() {
        int randomNumber = 0;
        while (randomNumber < 5) {
            randomNumber = random.nextInt(100);
        }

        return randomNumber * 1000;
    }

    public static int payAmount(int orderAmount) {
        int result = random.nextInt(100);
        if (result < 50) {
            return orderAmount;
        }
        else if (result < 80) {
            return orderAmount - 1000;
        }
        else {
            return orderAmount - 2000;
        }
    }
}
