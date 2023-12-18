package com.example.demo.message.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
@Slf4j
@Getter
@NoArgsConstructor
@Entity
public class QMessage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long messageId;
    @Enumerated(EnumType.STRING)
    private TaskType taskType;
    @Enumerated(EnumType.STRING)
    private MessageStatus messageStatus;
    @Embedded
    private Requester requester;
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    public QMessage(TaskType taskType, MessageStatus messageStatus, Requester requester, ServiceType serviceType) {
        this.taskType = taskType;
        this.messageStatus = messageStatus;
        this.requester = requester;
        this.serviceType = serviceType;
    }
    public static QMessage sentToOrderService(TaskType taskType, Requester requester) {
        return new QMessage(taskType, MessageStatus.SENT, requester, ServiceType.ORDER);
    }

    public void changeMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }

    public boolean messageStatus(MessageStatus messageStatus) {
        return this.messageStatus.equals(messageStatus);
    }

    public boolean receiverType(ServiceType serviceType) {
        return this.serviceType.equals(serviceType);
    }

    @Override
    public String toString() {
        return "QMessage{" +
                "messageId=" + messageId +
                ", taskType=" + taskType +
                ", messageStatus=" + messageStatus +
                ", requester=" + requester +
                ", receiverType=" + serviceType +
                '}';
    }
}
