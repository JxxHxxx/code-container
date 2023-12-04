package com.example.demo.pay.batch.presentation;

import com.example.demo.pay.batch.application.BatchManager;
import com.example.demo.pay.batch.dto.response.SimpleBatchResponse;
import com.example.demo.pay.batch.application.dto.SimpleBatchServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BatchApiController {
    private final BatchManager batchManager;
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
}
