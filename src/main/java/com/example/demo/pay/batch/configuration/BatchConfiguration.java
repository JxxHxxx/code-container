package com.example.demo.pay.batch.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;
import java.util.Collection;

/**
 * 잡 등록 설정 - 재 시작 시 필요
 * 재 시작 시, JobRegistry 에서 재 시작할 잡 클래스를 찾음
 * JobRegistry 에 잡을 등록할 필요가 있음, ReferenceJobFactory 에 의해 주입됨
 * 시점은 : 의존성 주입 완료 후 ~ 애플리케이션 로딩이 완료되기 전 사이
 */

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {
    private final JobRegistry jobRegistry;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() throws Exception {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        jobRegistryBeanPostProcessor.afterPropertiesSet();
        return jobRegistryBeanPostProcessor;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void checkJobNames() {
        Collection<String> jobNames = jobRegistry.getJobNames();
        if (jobNames.isEmpty()) {
            log.warn("jobRegistry 에 Job 들이 등록되지 않았습니다. 확인하시길 바랍니다.");
        }
        log.info("=================== Enrolled jobRegistry Job List start ===================");
        jobNames.forEach(jobName -> log.info("jobName : {} ", jobName));
        log.info("=================== Enrolled jobRegistry Job List  end  ===================");
    }
}
