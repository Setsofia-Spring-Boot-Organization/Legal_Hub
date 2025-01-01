package com.example.legal_hub.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LegalHubException extends RuntimeException {

    final Error error;

    public LegalHubException(Error error) {
        super(error.label);
        this.error = error;
    }

    public LegalHubException(Error error, Throwable cause) {
        super(error.label, cause);
        this.error = error;
    }
}
