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
    private ReceiverType receiverType;

    public QMessage(TaskType taskType, MessageStatus messageStatus, Requester requester, ReceiverType receiverType) {
        this.taskType = taskType;
        this.messageStatus = messageStatus;
        this.requester = requester;
        this.receiverType = receiverType;
    }

    public QMessage(TaskType taskType, Requester requester, ReceiverType receiverType) {
        this.taskType = taskType;
        this.requester = requester;
        this.receiverType = receiverType;
    }

    public void changeMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }

    public boolean messageStatus(MessageStatus messageStatus) {
        return this.messageStatus.equals(messageStatus);
    }

    public boolean receiverType(ReceiverType receiverType) {
        return this.receiverType.equals(receiverType);
    }

    @Override
    public String toString() {
        return "QMessage{" +
                "messageId=" + messageId +
                ", taskType=" + taskType +
                ", messageStatus=" + messageStatus +
                ", requester=" + requester +
                ", receiverType=" + receiverType +
                '}';
    }
}
