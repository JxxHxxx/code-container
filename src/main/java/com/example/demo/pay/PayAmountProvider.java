package com.example.demo.pay;

import java.util.Random;

public class PayAmountProvider {

    private static Random random = new Random();

    public static int execute(int unit) {
        int randomNumber = 0;
        while (randomNumber == 0) {
            randomNumber = random.nextInt(100);
        }

        return randomNumber * unit;
    }
}
