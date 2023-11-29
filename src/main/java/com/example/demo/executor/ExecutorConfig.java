package com.example.demo.executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//@Configuration
public class ExecutorConfig {

    private final int corePoolSize = 10;
    private final int maximumPoolSize = 10;
    private final int keepAliveTime = 60;

//    @Bean
    public ExecutorService executorService() {
        ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue(10);
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                blockingQueue);

    }
}
