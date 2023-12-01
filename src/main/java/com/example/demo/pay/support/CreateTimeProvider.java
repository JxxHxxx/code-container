package com.example.demo.pay.support;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

public class CreateTimeProvider {

    private static final Random random = new Random();

    private static final int HOUR_BOUND = 24;
    private static final int MINUTE_BOUND = 60;
    private static final int SECOND_BOUND = 60;

    public static LocalDateTime createLocalDateTime() {
        return LocalDateTime.of(createLocalDate(), createLocalTime());
    }

    public static LocalDate createLocalDate() {
        LocalDate localDate = LocalDate.now();
        int dayToSubtract = random.nextInt(11);
        return localDate.minusDays(dayToSubtract);
    }
    public static LocalTime createLocalTime() {
        int hour = random.nextInt(HOUR_BOUND);
        int minute = random.nextInt(MINUTE_BOUND);
        int second = random.nextInt(SECOND_BOUND);
        return LocalTime.of(hour, minute, second);
    }
}
