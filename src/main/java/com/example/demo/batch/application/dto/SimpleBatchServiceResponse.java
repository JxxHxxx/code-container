package com.example.demo.batch.application.dto;

import lombok.Getter;
import org.springframework.batch.core.JobParameters;

@Getter
public class SimpleBatchServiceResponse {
    private final String jobName;
    private final long executionId;
    private final JobParameters jobParameters;

    public SimpleBatchServiceResponse(String jobName, long executionId, JobParameters jobParameters) {
        this.jobName = jobName;
        this.executionId = executionId;
        this.jobParameters = jobParameters;
    }
}
