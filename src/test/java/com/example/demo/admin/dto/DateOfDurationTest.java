package com.example.demo.admin.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class DateOfDurationTest {

    @Test
    void test() {
        LocalDate parse = LocalDate.parse("2023-12-01");
        System.out.println(parse);
        LocalDateTime start = parse.atStartOfDay();
        System.out.println(start);
        LocalDateTime end = parse.atTime(23, 59, 59);
        System.out.println(end);
    }
}