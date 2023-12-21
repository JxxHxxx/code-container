package com.example.demo.admin.presentation;

import com.example.demo.admin.application.dto.JobExecutionServiceResponse;
import com.example.demo.batch.application.JobMonitoringService;
import com.example.demo.batch.dto.response.SimpleBatchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminJobMonitoringController {

    private final JobMonitoringService jobMonitoringService;

    @GetMapping("/batch")
    public ResponseEntity<SimpleBatchResponse> readJobExecutions(@RequestParam(name = "job-name") String jobName) {
        log.info("request param {}", jobName);
        List<JobExecutionServiceResponse> serviceResponses = jobMonitoringService.getExecutionResults(jobName);

        return ResponseEntity.ok(new SimpleBatchResponse(jobName + " 잡 실행 결과" , serviceResponses));
    }
}
