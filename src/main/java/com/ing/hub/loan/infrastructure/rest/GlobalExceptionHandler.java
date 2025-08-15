package com.ing.hub.loan.infrastructure.rest;

import com.ing.hub.loan.domain.exception.BusinessException;
import com.ing.hub.loan.domain.exception.NotFoundException;
import com.ing.hub.loan.domain.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, ServletWebRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(ex.getMessage(), req.getRequest().getRequestURI()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusiness(BusinessException ex, ServletWebRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(ex.getMessage(), req.getRequest().getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, ServletWebRequest req) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + " " + f.getDefaultMessage())
                .findFirst().orElse("Validation error");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(msg, req.getRequest().getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleOther(Exception ex, ServletWebRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(ex.getMessage(), req.getRequest().getRequestURI()));
    }
}
