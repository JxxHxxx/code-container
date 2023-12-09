package com.example.demo.batch.mapper;

public class BatchExecuteException extends RuntimeException{
    public BatchExecuteException(String message) {
        super(message);
    }

    public BatchExecuteException(String message, Throwable cause) {
        super(message, cause);
    }
}
