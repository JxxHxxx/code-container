package com.example.demo.pay;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class IdentifyProvider {
    public static String generateStringId(int range) {
        return String.valueOf(new Random().nextInt(range));
    }
}
