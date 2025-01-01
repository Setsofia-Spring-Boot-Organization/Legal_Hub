package com.example.legal_hub.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LegalHubException.class)
    public ResponseEntity<ExceptionResponse> handleExceptions(LegalHubException exception, WebRequest request) {
        HttpStatus status = HttpStatus.OK; // set the default http status
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        Error error = exception.error;
        String cause = null;
        if (exception.getCause() != null) {
            cause = exception.getCause().getMessage();
        }

        switch (error) {
            case INVALID_BUNDLE_ID,
                 ERROR_SAVING_DATA -> status = HttpStatus.BAD_REQUEST;
        }

        ExceptionResponse response = new ExceptionResponse(
                status.value(),
                error.label,
                cause,
                path
        );

        return new ResponseEntity<>(response, status);
    }
}
