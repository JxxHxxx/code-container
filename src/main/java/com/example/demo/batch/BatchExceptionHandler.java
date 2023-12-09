package com.example.demo.batch;

import com.example.demo.batch.mapper.BatchExecuteException;
import com.example.demo.batch.presentation.BatchApiController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice(basePackageClasses = BatchApiController.class)
public class BatchExceptionHandler {

    @ExceptionHandler(FlatFileParseException.class)
    public ResponseEntity<String> handleFlatFileParseException(FlatFileParseException exception) {
        log.error("error", exception);
        return ResponseEntity.badRequest().body("올바르지 않은 값이...!");
    }

    @ExceptionHandler(BatchExecuteException.class)
    public ResponseEntity<String> handleIllegalArgumentException(BatchExecuteException exception) {
        log.error("error", exception);
        return ResponseEntity.badRequest().body("파일 내 형식에 맞지 않은 값이 존재합니다.");
    }
}
