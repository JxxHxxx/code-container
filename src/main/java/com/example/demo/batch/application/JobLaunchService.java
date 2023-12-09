package com.example.demo.batch.application;

import com.example.demo.batch.application.dto.JobResultResponse;
import com.example.demo.batch.dto.request.JobLauncherRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobLaunchService {

    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
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

}
