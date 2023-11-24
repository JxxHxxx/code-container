package com.example.demo.message.application;

import com.example.demo.message.domain.MessageStatus;
import com.example.demo.message.domain.QMessageHistory;
import com.example.demo.message.dto.QMessageHistoryDto;
import com.example.demo.message.infra.QMessageHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class QMessageHistoryService {

    private final QMessageHistoryRepository repository;

    /** size 설정
     * @return
     */

    public List<QMessageHistoryDto> readQMessageHistories(MessageStatus messageStatus) {
        List<QMessageHistory> qMessageHistories = repository.readBy(messageStatus);

        return qMessageHistories.stream()
                .map(history -> transformToDto(history))
                .collect(toList());
    }

    public QMessageHistoryDto readQMessageHistory(Long historyId) {
        QMessageHistory qMessageHistory = repository.findById(historyId)
                .orElseThrow(() -> new IllegalArgumentException());

        return transformToDto(qMessageHistory);
    }

    private QMessageHistoryDto transformToDto(QMessageHistory history) {
        return new QMessageHistoryDto(
                history.getId(),
                history.getQMessageId(),
                history.getTaskType(),
                history.getMessageStatus(),
                history.requesterId(),
                history.requestTime(),
                history.getExecuteTime());
    }

}
