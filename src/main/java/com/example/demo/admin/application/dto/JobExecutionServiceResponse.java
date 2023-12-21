package com.example.demo.admin.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class JobExecutionServiceResponse {

    private final Long instanceId;
    private final Long executionId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String executionResult;
}
