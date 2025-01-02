package com.example.legal_hub.exceptions;

public enum Error {

    ERROR_SAVING_DATA("an error occur while saving data"),
    INVALID_BUNDLE_ID("the submitted bundle id is invalid"),
    ERROR_UPDATING_DATA("an error occur while updating data");

    public final String label;
    Error(String label) {
        this.label = label;
    }
}
