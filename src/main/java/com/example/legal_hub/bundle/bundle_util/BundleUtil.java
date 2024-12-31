package com.example.legal_hub.bundle.bundle_util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Component
public class BundleUtil {

    private final GridFsTemplate fsTemplate;
    private final GridFsOperations fsOperations;

    public BundleUtil(GridFsTemplate fsTemplate, GridFsOperations fsOperations) {
        this.fsTemplate = fsTemplate;
        this.fsOperations = fsOperations;
    }

    /**
     * This method uploads a PDF file to GridFS and returns the file ID.
     *
     * @param file The PDF file to upload.
     * @return The ID of the uploaded file in GridFS.
     * @throws IOException if an I/O error occurs while reading the file.
     * @throws IllegalArgumentException if the file is invalid or not a PDF.
     */
    public String uploadBundle(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("The file is null or empty");
        }

        if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }

        DBObject metadata = new BasicDBObject();
        metadata.put("type", "pdf");
        metadata.put("file_name", file.getOriginalFilename());
        metadata.put("size", file.getSize());

        ObjectId id = fsTemplate.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType(),
                metadata
        );
        return id.toString();
    }


    /**
     * This method retrieves a file from GridFS using its ID.
     *
     * @param id The ID of the file in GridFS.
     * @return An InputStreamResource representing the file content.
     * @throws IOException if an I/O error occurs while retrieving the file.
     */
    public InputStreamResource downloadBundle(String id) throws IOException {
        GridFSFile fsFile = fsTemplate.findOne(new Query(Criteria.where("_id").is(id)));

        if (fsFile == null) {
            throw new IllegalArgumentException("File not found with ID: " + id);
        }

        return new InputStreamResource(fsOperations.getResource(fsFile).getInputStream());
    }
}
