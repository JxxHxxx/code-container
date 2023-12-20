package com.example.demo.batch.job.processor;

public class UnableToProcessException extends RuntimeException{
    public UnableToProcessException(String message) {
        super(message);
    }
}
