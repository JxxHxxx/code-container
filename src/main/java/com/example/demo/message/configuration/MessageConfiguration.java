package com.example.demo.message.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;

@Slf4j
@Configuration
public class MessageConfiguration {

    private final int QUEUE_CAPACITY = 5;
    @Bean
    public QueueChannel messageChannel() {
        QueueChannel queueChannel = new QueueChannel(QUEUE_CAPACITY);

        return queueChannel;
    }
}
