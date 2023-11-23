package com.example.demo.message.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.jpa.core.JpaExecutor;
import org.springframework.integration.jpa.inbound.JpaPollingChannelAdapter;

import javax.persistence.EntityManagerFactory;

@Slf4j
@Configuration
public class MessageConfiguration {

    private final int QUEUE_CAPACITY = 5;
    @Bean
    public QueueChannel messageChannel() {
        QueueChannel queueChannel = new QueueChannel(QUEUE_CAPACITY);
        return queueChannel;
    }
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public JpaExecutor jpaExecutor() {
        JpaExecutor executor = new JpaExecutor(entityManagerFactory);
        // executor 설정 필요
        executor.setJpaQuery("SELECT m FROM QMessage m ");
        return executor;
    }

    /** Provider 역할
     *  send to messageChannel
     */
    @Bean
    @InboundChannelAdapter(channel = "messageChannel", poller = @Poller(fixedDelay = "${poller.interval}"))
    public MessageSource<?> qMessageSource() {
        return new JpaPollingChannelAdapter(jpaExecutor());
    }
}

