package com.example.legal_hub.bundle;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Document(collection = "bundle")
public class Bundle {

    @MongoId(FieldType.STRING)
    private String id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String author;

    private String title;
    private String description;
    private String category;

    public Bundle(LocalDateTime createdAt, LocalDateTime updatedAt, String author, String title, String description, String category) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.author = author;
        this.title = title;
        this.description = description;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bundle bundle = (Bundle) o;
        return Objects.equals(id, bundle.id) && Objects.equals(createdAt, bundle.createdAt) && Objects.equals(updatedAt, bundle.updatedAt) && Objects.equals(author, bundle.author) && Objects.equals(title, bundle.title) && Objects.equals(description, bundle.description) && Objects.equals(category, bundle.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, updatedAt, author, title, description, category);
    }

    @Override
    public String toString() {
        return "Bundle{" +
                "id='" + id + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
