package com.example.demo.pay.batch.schedule;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;

@Configuration
public class QuartzConfiguration {

    @Bean
    public JobDetail quartzJobDetail() {
        return JobBuilder.newJob(BatchScheduledJob.class)
                .storeDurably()
                .build();
    }

    /***
     * Seconds - Minutes - Hours - Day-of-month - Month - Day-of-Week - Year (Optional)
     */
    @Bean
    public Trigger jobTrigger() throws ParseException {
        CronExpression cronExpression = new CronExpression("0 28 15 ? * SUN-SAT");
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        return TriggerBuilder.newTrigger()
                .forJob(quartzJobDetail())
                .withSchedule(cronScheduleBuilder)
                .build();
    }
}
