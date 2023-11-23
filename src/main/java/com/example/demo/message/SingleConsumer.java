package com.example.demo.message;

import com.example.demo.message.domain.QMessage;
import com.example.demo.message.domain.QMessageHistory;
import com.example.demo.message.infra.QMessageHistoryRepository;
import com.example.demo.message.infra.QMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.message.domain.QMessageHistory.*;

@Slf4j
@MessageEndpoint
@RequiredArgsConstructor
public class SingleConsumer {
    private final QMessageRepository qMessageRepository;
    private final QMessageHistoryRepository qMessageHistoryRepository;

//    @ServiceActivator(inputChannel = "messageChannel")
//    public void handleMessage(List<QMessage> messages) throws InterruptedException {
//        log.info("=======================================");
//        messages.forEach(message -> log.info("Received message : {}", message));
//        log.info("=======================================");
//        Thread.sleep(500);
//    }

    @Transactional
    @ServiceActivator(inputChannel = "messageChannel")
    public void handleMessage(List<QMessage> messages) throws InterruptedException {
        messages.forEach(qMessage -> {
            log.info("=====================================");
            someBusinessLogic(qMessage);

            createQMessageHistory(qMessage);
            qMessageRepository.delete(qMessage);
            qMessageRepository.flush();
            log.info("=====================================");
        });

        Thread.sleep(50);
    }

    private void createQMessageHistory(QMessage qMessage) {
        QMessageHistory qMessageHistory = builder()
                .QMessageId(qMessage.getMessageId())
                .messageStatus(qMessage.getMessageStatus())
                .requester(qMessage.getRequester())
                .taskType(qMessage.getTaskType())
                .build();

        qMessageHistoryRepository.saveAndFlush(qMessageHistory);
    }

    private static void someBusinessLogic(QMessage qMessage) {
        log.info("{}", qMessage);
    }
}
