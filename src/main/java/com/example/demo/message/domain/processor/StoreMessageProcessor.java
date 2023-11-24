package com.example.demo.message.domain.processor;

import com.example.demo.message.domain.QMessage;
import com.example.demo.message.domain.TaskResult;
import com.example.demo.message.infra.QMessageHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class StoreMessageProcessor {

    private final QMessageHistoryRepository qMessageHistoryRepository;

    public TaskResult execute(QMessage qMessage) {
        log.info("미구현입니다.");
        return null;
    }
}
