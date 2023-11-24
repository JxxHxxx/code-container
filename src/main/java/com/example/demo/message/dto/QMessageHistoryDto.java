package com.example.demo.message.dto;

import com.example.demo.message.domain.MessageStatus;
import com.example.demo.message.domain.TaskType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter

public class QMessageHistoryDto {

    private final Long QMessageHistoryId;
    private final Long QMessageId;
    private final TaskType taskType;
    private final MessageStatus messageStatus;
    private final String requesterId;
    private final LocalDateTime requestTime;
    private final LocalDateTime executeTime;

    public QMessageHistoryDto(Long QMessageHistoryId, Long QMessageId, TaskType taskType, MessageStatus messageStatus,
                              String requesterId, LocalDateTime requestTime, LocalDateTime executeTime) {
        this.QMessageHistoryId = QMessageHistoryId;
        this.QMessageId = QMessageId;
        this.taskType = taskType;
        this.messageStatus = messageStatus;
        this.requesterId = requesterId;
        this.requestTime = requestTime;
        this.executeTime = executeTime;
    }
}
