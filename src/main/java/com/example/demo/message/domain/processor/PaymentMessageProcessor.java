package com.example.demo.message.domain.processor;

import com.example.demo.message.application.OrderMessageService;
import com.example.demo.message.domain.QMessage;
import com.example.demo.message.domain.QMessageHistory;
import com.example.demo.message.domain.TaskResult;
import com.example.demo.message.infra.QMessageHistoryRepository;
import com.example.demo.message.infra.QMessageRepository;
import com.example.demo.message.model.OrderMessageParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static com.example.demo.message.domain.MessageStatus.*;
import static com.example.demo.message.domain.MessageStatus.RETRY;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentMessageProcessor {

    private final QMessageRepository qMessageRepository;
    private final QMessageHistoryRepository qMessageHistoryRepository;
    private final OrderMessageService orderMessageService;


    @Transactional
    public TaskResult execute(QMessage qMessage) {
        if (qMessage.messageStatus(SENT)) {
            qMessage.changeMessageStatus(PROCESSING);
            log.info("주문 서버 메시지 전송 작업 타입 {}", qMessage.getTaskType().getFullName()); // bz logic
            try {
                //
                OrderMessageParam orderMessageParam = OrderMessageParam.create(qMessage.getOrderNo(), qMessage.getTaskType());
                orderMessageService.sendInsertQuery(orderMessageParam);
                qMessage.changeMessageStatus(SUCCESS);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            createQMessageHistory(qMessage);
            qMessageRepository.delete(qMessage);
            return TaskResult.FINISHED;
        }

        if (qMessage.messageStatus(SUCCESS)) {
            log.info("이미 수신이 완료된 메시지 입니다. messageId : {}", qMessage.getMessageId()); // info

            return TaskResult.FINISHED;
        }

        if (qMessage.messageStatus(FAIL)) {
            log.info("해당 메시지는 송신에 실패했습니다. 개별 동기화 혹은 재동기 처리를 이용하십시오"); // info
            return TaskResult.SKIP;
        }

        if (qMessage.messageStatus(RETRY)) {
            log.info("재동기 처리 중인 메시지입니다.");
            return TaskResult.SKIP;
        }
        return TaskResult.FINISHED;
    }

    private void createQMessageHistory(QMessage qMessage) {
        QMessageHistory qMessageHistory = new QMessageHistory(
                qMessage.getMessageId(),
                qMessage.getOrderNo(),
                qMessage.getTaskType(),
                qMessage.getMessageStatus(),
                qMessage.getRequester());
        qMessageHistoryRepository.saveAndFlush(qMessageHistory);
    }
}
