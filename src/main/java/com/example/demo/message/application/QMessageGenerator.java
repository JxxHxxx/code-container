package com.example.demo.message.application;

import com.example.demo.message.domain.MessageStatus;
import com.example.demo.message.domain.QMessage;
import com.example.demo.message.domain.Requester;
import com.example.demo.message.dto.QMessageForm;
import com.example.demo.message.infra.QMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QMessageGenerator {
    private final QMessageRepository qMessageRepository;

    @Transactional
    public void execute(QMessageForm qMessageForm) {
        QMessage qMessage = new QMessage(
                qMessageForm.getTaskType(),
                MessageStatus.SENT,
                new Requester(qMessageForm.getRequesterId()),
                qMessageForm.getReceiverType());

        qMessageRepository.save(qMessage);
    }


}
