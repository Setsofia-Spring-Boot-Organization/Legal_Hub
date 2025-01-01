package com.example.legal_hub.bundle.responses;

import java.time.LocalDateTime;

public record BundleData(
        String id,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,

        String author,

        String title,
        String description,
        String category,

        String downloadURL
) { }
