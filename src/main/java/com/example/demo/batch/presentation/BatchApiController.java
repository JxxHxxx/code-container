package com.example.demo.batch.presentation;

import com.example.demo.batch.application.BatchManager;
import com.example.demo.batch.application.dto.SimpleBatchServiceResponse;
import com.example.demo.batch.dto.response.SimpleBatchResponse;
import com.example.demo.batch.dto.request.JobLauncherRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BatchApiController {
    private final BatchManager batchManager;
    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final ApplicationContext context;

    @GetMapping("/batch/stop")
    public ResponseEntity<SimpleBatchResponse> stopBatch(@RequestParam long executionId) throws NoSuchJobExecutionException, JobExecutionNotRunningException {
        SimpleBatchServiceResponse serviceResponse = batchManager.stop(executionId);

        return ResponseEntity.ok(new SimpleBatchResponse<>("잡 중지", serviceResponse));
    }

    @PostMapping("/batch/restart")
    public ResponseEntity<SimpleBatchResponse> restartBatch(@RequestParam long executionId) throws Exception {
        SimpleBatchServiceResponse serviceResponse = batchManager.restart(executionId);
        return ResponseEntity.ok(new SimpleBatchResponse<>("잡 재시작(완료 X)", serviceResponse));
    }

    @PostMapping("/batch/run")
    public ExitStatus runJob(@RequestBody JobLauncherRequest request) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        log.info("START {}", request.getJobName());
        Job job = context.getBean(request.getJobName(), Job.class);
        JobParameters jobParameters = new JobParametersBuilder(request.getJobParameters(), jobExplorer)
                .getNextJobParameters(job)
                .toJobParameters();

        return jobLauncher.run(job, jobParameters).getExitStatus();
    }
}
