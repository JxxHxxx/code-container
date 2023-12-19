package com.example.demo.message.presentation;

import com.example.demo.message.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class SimpleApiController {

    private final Provider provider;

    @PostMapping("/queue")
    public String sendMessage() {
        return "Q 생성 완료";
    }
}
