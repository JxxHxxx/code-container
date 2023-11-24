package com.example.demo.message.dto;

import com.example.demo.message.domain.ReceiverType;
import com.example.demo.message.domain.TaskType;
import lombok.Getter;

@Getter
public class QMessageForm {
    private final TaskType taskType;
    private final String requesterId;
    private final ReceiverType receiverType;

    public QMessageForm(TaskType taskType, String requesterId, ReceiverType receiverType) {
        this.taskType = taskType;
        this.requesterId = requesterId;
        this.receiverType = receiverType;
    }
}
