package com.example.demo.pay;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

class PayTest {

    @Test
    void getTime() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
    }
}