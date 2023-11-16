package com.example.demo.pay;


import org.junit.Test;

import java.util.Random;

public class SimpleTest {

    @Test
    public void someTest() {

        int id = new Random(10).nextInt();
        System.out.println(id);
    }
}
