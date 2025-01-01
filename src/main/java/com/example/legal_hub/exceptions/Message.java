package com.example.legal_hub.exceptions;

public enum Message {

    THE_DATA_CANNOT_BE_SAVED_PLEASE_TRY_AGAIN("the bundle cannot be saved. Try again later");

    public final String label;
    Message(String label) {
        this.label = label;
    }
}
