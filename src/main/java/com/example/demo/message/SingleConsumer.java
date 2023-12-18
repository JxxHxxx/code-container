package com.example.demo.message;

import com.example.demo.message.domain.*;
import com.example.demo.message.domain.processor.PaymentMessageProcessor;
import com.example.demo.message.domain.processor.StoreMessageProcessor;
import com.example.demo.message.infra.QMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.message.domain.ServiceType.*;

@Slf4j
@MessageEndpoint
@RequiredArgsConstructor
public class SingleConsumer {
    private final QMessageRepository qMessageRepository;
    private final PaymentMessageProcessor paymentMessageProcessor;
    private final StoreMessageProcessor storeMessageProcessor;

    @Transactional
    @ServiceActivator(inputChannel = "messageChannel")
    public void handleMessage(List<QMessage> messages) throws InterruptedException {
        messages.forEach(qMessage -> {
            log.info("=================START====================");
            log.info("Message info : {}", qMessage);

            TaskResult taskResult = null;
            // 수신자 : 결제 서버
            if (qMessage.receiverType(ORDER)) {
                taskResult = paymentMessageProcessor.execute(qMessage);
            }
            // 수신자 : 그 외 서버
            if (qMessage.receiverType(OTHER)) {
                taskResult = storeMessageProcessor.execute(qMessage);
            }

            if (TaskResult.FINISHED.equals(taskResult)) {
                qMessageRepository.delete(qMessage);
                qMessageRepository.flush();
            }

            log.info("================= END ====================");
        });

        Thread.sleep(50);
    }
}
