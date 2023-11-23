package com.example.demo.message;

import com.example.demo.message.domain.QMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.List;

@Slf4j
@MessageEndpoint
public class SingleConsumer {

    @ServiceActivator(inputChannel = "messageChannel")
    public void handleMessage(List<QMessage> messages) throws InterruptedException {
        log.info("=======================================");
        messages.forEach(message -> log.info("Received message : {}", message));
        log.info("=======================================");
        Thread.sleep(500);
    }
}
