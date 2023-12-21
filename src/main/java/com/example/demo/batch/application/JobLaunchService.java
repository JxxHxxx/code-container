package com.example.demo.batch.application;

import com.example.demo.batch.application.dto.JobResultResponse;
import com.example.demo.batch.application.dto.SimpleBatchServiceResponse;
import com.example.demo.batch.dto.request.JobLauncherRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 잡 시작, 중지, 재시작
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class JobLaunchService {

    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final JobOperator jobOperator;
    private final ApplicationContext context;

    public JobResultResponse executeJob(JobLauncherRequest request) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        log.info("START {}", request.getJobName());

        Job job = context.getBean(request.getJobName(), Job.class);
        JobParameters jobParameters = new JobParametersBuilder(request.getJobParameters(), jobExplorer)
                .getNextJobParameters(job)
                .toJobParameters();

        ExitStatus exitStatus = jobLauncher.run(job, jobParameters)
                .getExitStatus();

        return new JobResultResponse(exitStatus.getExitCode());
    }

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
