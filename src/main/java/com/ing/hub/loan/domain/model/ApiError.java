package com.ing.hub.loan.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiError {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String message;
    private final String path;

    public ApiError(String message, String path) {
        this.message = message;
        this.path = path;
    }
}
