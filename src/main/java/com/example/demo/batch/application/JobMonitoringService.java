package com.example.demo.batch.application;

import com.example.demo.admin.application.dto.JobExecutionServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.*;

/**
 * 배치 결과 관련 기능만 조회
 */


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JobMonitoringService {

    private final JobExplorer jobExplorer;

    public List<JobExecutionServiceResponse> getExecutionResults(String jobName) {
        List<JobInstance> jobInstances = jobExplorer.findJobInstancesByJobName(jobName, 0, 100);

        List<JobExecution> jobExecutions = jobInstances.stream()
                .map(jobInstance -> jobExplorer.getJobExecutions(jobInstance))
                .flatMap(Collection::stream)
                .collect(toList());

        List<JobExecutionServiceResponse> responses = new ArrayList<>();
        for (JobExecution jobExecution : jobExecutions) {
            // 모든 잡은 종료상태가 아닐 수 있기에 EndTime 이 NULL 일 가능성이 존재한다.
            if (jobExecution.getStartTime() == null || jobExecution.getEndTime() == null || jobExecution.getExitStatus() == null) {
                log.info("skip");
                continue;
            }
            // 그 외의 경우에는 매핑해서 넘김
            else {
                LocalDateTime startTime = jobExecution.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime endTime = jobExecution.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                String exitCode = jobExecution.getExitStatus().getExitCode();
                long instanceId = jobExecution.getJobInstance().getInstanceId();
                Long jobId = jobExecution.getJobId();

                responses.add(new JobExecutionServiceResponse(instanceId,jobId, startTime, endTime, exitCode));
            }
        }

        return responses;
    }
}
