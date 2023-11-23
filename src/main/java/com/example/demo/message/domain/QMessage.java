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

    public QMessage(long messageId, TaskType taskType, Requester requester) {
        log.info("CALLED QMessage CONSTRUCTOR");
        this.messageId = messageId;
        this.taskType = taskType;
        this.requester = requester;
        this.messageStatus = MessageStatus.SENT;
    }

    @Override
    public String toString() {
        return "QMessage{" +
                "messageId=" + messageId +
                ", taskType=" + taskType +
                ", messageStatus=" + messageStatus +
                ", requester=" + requester +
                '}';
    }
}
