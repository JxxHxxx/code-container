package com.example.demo.message.dto;

import com.example.demo.message.domain.ServiceType;
import com.example.demo.message.domain.TaskType;
import lombok.Getter;

@Getter
public class QMessageForm {
    private final TaskType taskType;
    private final String requesterId;
    private final ServiceType serviceType;

    public QMessageForm(TaskType taskType, String requesterId, ServiceType serviceType) {
        this.taskType = taskType;
        this.requesterId = requesterId;
        this.serviceType = serviceType;
    }
}
