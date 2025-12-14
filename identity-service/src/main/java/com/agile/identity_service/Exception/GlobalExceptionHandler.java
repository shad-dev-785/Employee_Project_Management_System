package com.agile.identity_service.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
@ExceptionHandler
    public ResponseEntity<Map<String, String>> handleEpmsException(EpmsException ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(409)
                .body(Map.of("error", ex.getMessage()));
    }
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(500)
                .body(Map.of("error", ex.getMessage()));
    }
}
