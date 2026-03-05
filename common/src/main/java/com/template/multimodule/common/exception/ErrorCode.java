package com.template.multimodule.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "Invalid input value"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "Method not allowed"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "Internal server error"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C004", "Invalid type value"),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "C005", "Entity not found"),

    // Sample
    SAMPLE_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "Sample not found"),
    SAMPLE_ALREADY_EXISTS(HttpStatus.CONFLICT, "S002", "Sample already exists");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
