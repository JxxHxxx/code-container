package com.example.demo.pay.batch.application;

import com.example.demo.pay.batch.application.dto.SimpleBatchServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchManager {

    private final JobOperator jobOperator;
    private final JobExplorer jobExplorer;
    private final ExecutorService executorService = new ThreadPoolExecutor(
            10,10,60, TimeUnit.SECONDS,
            new ArrayBlockingQueue(10));


    public SimpleBatchServiceResponse stop(long executionId) throws NoSuchJobExecutionException, JobExecutionNotRunningException {
        jobOperator.stop(executionId);
        log.info("STOP JOB executionId {}", executionId);

        JobExecution jobExecution = jobExplorer.getJobExecution(executionId);
        String jobName = jobExecution.getJobInstance().getJobName();
        JobParameters jobParameters = jobExecution.getJobParameters();
        return new SimpleBatchServiceResponse(jobName, executionId, jobParameters);
    }

    public SimpleBatchServiceResponse restart(long executionId) throws JobInstanceAlreadyCompleteException, NoSuchJobException, NoSuchJobExecutionException, JobParametersInvalidException, JobRestartException {
        JobExecution jobExecution = jobExplorer.getJobExecution(executionId);
        String jobName = jobExecution.getJobInstance().getJobName();
        JobParameters jobParameters = jobExecution.getJobParameters();
        jobOperator.restart(executionId);

        return new SimpleBatchServiceResponse(jobName, executionId, jobParameters);
    }

    private void asyncRestartBatch(long executionId) {
        log.info("RESTART JOB executionId {}", executionId);
        try {
            jobOperator.restart(executionId);
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new RuntimeException(e);
        } catch (NoSuchJobExecutionException e) {
            throw new RuntimeException(e);
        } catch (NoSuchJobException e) {
            throw new RuntimeException(e);
        } catch (JobRestartException e) {
            throw new RuntimeException(e);
        } catch (JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
        log.info("COMPLETE JOB executionId {}", executionId);
    }
}
