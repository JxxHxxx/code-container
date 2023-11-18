package com.example.demo.pay.presentation;

import com.example.demo.pay.dto.JobLauncherRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PayBatchController {

    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final ApplicationContext context;

    @PostMapping("/batch/run")
    public ExitStatus runJob(@RequestBody JobLauncherRequest request) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Job job = context.getBean(request.getJobName(), Job.class);
        JobParameters jobParameters = new JobParametersBuilder(request.getJobParameters(), jobExplorer)
                .getNextJobParameters(job)
                .toJobParameters();

        return jobLauncher.run(job, jobParameters).getExitStatus();
    }
}
