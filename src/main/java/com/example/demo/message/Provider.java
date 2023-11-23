package com.example.demo.message;

import com.example.demo.message.domain.QMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Provider {

    private final QueueChannel queueChannel;


    public void sendMessage(QMessage message) {
        log.info("queueChannel now size {}", queueChannel.getQueueSize());
        Message<QMessage> qMessage = MessageBuilder.withPayload(message)
                .build();
        this.queueChannel.send(qMessage);
    }
}
