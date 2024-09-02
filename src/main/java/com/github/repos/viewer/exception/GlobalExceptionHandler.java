package com.github.repos.viewer.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String USER_NOT_FOUND_ERROR = " The requested GitHub user cannot be found.";

    @ExceptionHandler(WebClientResponseException.class)
    @ResponseBody
    public Map<String, String> handleWebClientResponseException(WebClientResponseException e) {
        return Map.of(
                "message", e.getMessage() + USER_NOT_FOUND_ERROR,
                "status", String.valueOf(e.getStatusCode().value())
        );
    }

}
