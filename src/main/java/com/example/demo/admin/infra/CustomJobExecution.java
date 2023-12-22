package com.example.demo.admin.infra;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomJobExecution {
    private Long jobExecutionId;

    private String jobName;
    private Long jobInstanceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String exitCode;

}
