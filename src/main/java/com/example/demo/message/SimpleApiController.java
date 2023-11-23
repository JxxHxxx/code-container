package com.example.demo.message;

import com.example.demo.message.domain.QMessage;
import com.example.demo.message.domain.Requester;
import com.example.demo.message.domain.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SimpleApiController {

    private final Provider provider;


    @PostMapping("/queue")
    public String sendMessage() {
        Requester requester = new Requester(UUID.randomUUID().toString().substring(0, 6));
        QMessage qMessage = new QMessage(1l, TaskType.I, requester);
        provider.sendMessage(qMessage);
        return "Q 생성 완료";
    }
}
