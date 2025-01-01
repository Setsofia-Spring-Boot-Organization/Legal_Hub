package com.example.legal_hub.bundle.requests;

public record UploadNewBundle(
        String author,
        String title,
        String description,
        String category
) { }
