package com.example.demo.pay.batch.dto.response;

import lombok.Getter;

@Getter
public class SimpleBatchResponse<T> {
    private final String description;
    private final T data;

    public SimpleBatchResponse(String description, T data) {
        this.description = description;
        this.data = data;
    }
}
