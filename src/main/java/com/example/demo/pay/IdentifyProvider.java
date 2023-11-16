package com.example.demo.pay;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class IdentifyProvider {
    public static String generate(int range) {
        int id = new Random().nextInt(10);
        return String.valueOf(id);
    }
}
