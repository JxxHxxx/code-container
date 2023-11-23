package com.example.demo.message;

import com.example.demo.message.domain.QMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@Slf4j
@MessageEndpoint
public class Consumer {

    @ServiceActivator(inputChannel = "messageChannel")
    public void handleMessage(QMessage message) throws InterruptedException {
        log.info("Received message: {}", message);
        Thread.sleep(500);
    }
}
