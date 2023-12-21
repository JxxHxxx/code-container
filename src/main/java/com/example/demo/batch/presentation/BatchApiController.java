package com.example.demo.batch.presentation;

import com.example.demo.batch.application.JobLaunchService;
import com.example.demo.batch.application.dto.JobResultResponse;
import com.example.demo.batch.application.dto.SimpleBatchServiceResponse;
import com.example.demo.batch.dto.response.SimpleBatchResponse;
import com.example.demo.batch.dto.request.JobLauncherRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BatchApiController {
    private final JobLaunchService jobLaunchService;

    @PostMapping("/batch/run")
    public ResponseEntity<JobResultResponse> runJob(@RequestBody JobLauncherRequest request) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobResultResponse jobResultResponse = jobLaunchService.executeJob(request);

        return ResponseEntity.ok(jobResultResponse);
    }

    @GetMapping("/batch/stop")
    public ResponseEntity<SimpleBatchResponse> stopBatch(@RequestParam long executionId) throws NoSuchJobExecutionException, JobExecutionNotRunningException {
        SimpleBatchServiceResponse serviceResponse = jobLaunchService.stop(executionId);

        return ResponseEntity.ok(new SimpleBatchResponse<>("잡 중지", serviceResponse));
    }

    @PostMapping("/batch/restart")
    public ResponseEntity<SimpleBatchResponse> restartBatch(@RequestParam long executionId) throws Exception {
        SimpleBatchServiceResponse serviceResponse = jobLaunchService.restart(executionId);
        return ResponseEntity.ok(new SimpleBatchResponse<>("잡 재시작(완료 X)", serviceResponse));
    }
}
