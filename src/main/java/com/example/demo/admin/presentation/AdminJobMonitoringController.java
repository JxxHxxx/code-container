package com.example.demo.admin.presentation;

import com.example.demo.admin.application.dto.JobExecutionServiceResponse;
import com.example.demo.admin.dto.request.DateOfDuration;
import com.example.demo.admin.dto.response.JobExecutionResponse;
import com.example.demo.batch.application.JobMonitoringService;
import com.example.demo.batch.dto.response.SimpleBatchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminJobMonitoringController {

    private final JobMonitoringService jobMonitoringService;

    /**
     * 일자 별 배치 결과 - Admin 페이지에서 당일 배치 결과 확인
     * 완료 여부 - STOPPED, FAIL, UNKNOWN
     */
    // 동적 쿼리 만드는게 낫겠다.
    @GetMapping("/admin/job-executions")
    public ResponseEntity<SimpleBatchResponse> readJobResultByDate(@RequestBody DateOfDuration duration) {
        List<JobExecutionResponse> responses = jobMonitoringService.findExecutionsBy(duration.getStartDate(), duration.getEndDate());

        return ResponseEntity.ok(new SimpleBatchResponse("jobExecution 조회", responses));
    }

    @GetMapping("/admin/job-executions/cond")
    public ResponseEntity<SimpleBatchResponse> readJobResultByExitCode(@RequestParam("is-successful") boolean isSuccessful) {
        List<JobExecutionResponse> responses = jobMonitoringService.findExecutionsCond(isSuccessful);

        return ResponseEntity.ok(new SimpleBatchResponse("조건 조회", responses));
    }

    @GetMapping("/admin/batch")
    public ResponseEntity<SimpleBatchResponse> readJobExecutions(@RequestParam(name = "job-name") String jobName) {
        log.info("request param {}", jobName);
        List<JobExecutionServiceResponse> serviceResponses = jobMonitoringService.getExecutionResults(jobName);

        return ResponseEntity.ok(new SimpleBatchResponse(jobName + " 잡 실행 결과", serviceResponses));
    }
}
