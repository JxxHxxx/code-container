package com.example.demo.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
public class BatchExceptionHandler {

    @ExceptionHandler(FlatFileParseException.class)
    public ResponseEntity<String> handleFlatFileParseException(FlatFileParseException exception) {
        log.error("error", exception);
        return ResponseEntity.badRequest().body("올바르지 않은 값이...!");
    }
}
