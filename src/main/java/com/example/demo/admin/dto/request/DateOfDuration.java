package com.example.demo.admin.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Max;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class DateOfDuration {


    private final LocalDate startDate;
    private final LocalDate endDate;
}
