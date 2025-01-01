package com.example.legal_hub.bundle.bundle_util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Component
public class BundleUtil {

    private final GridFsTemplate fsTemplate;
    private final GridFSBucket fsBucket;

    public BundleUtil(GridFsTemplate fsTemplate, GridFSBucket fsBucket) {
        this.fsTemplate = fsTemplate;
        this.fsBucket = fsBucket;
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
     */
    public ResponseEntity<?> downloadBundle(String id) {
        try {
            // Validate and convert the string ID to ObjectId
            ObjectId objectId;
            try {
                objectId = new ObjectId(id);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file ID format");
            }

            // Retrieve the file from GridFS
            GridFSFile gridFsFile = fsTemplate.findOne(new Query(Criteria.where("_id").is(objectId)));

            if (gridFsFile == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
            }

            // Open the download stream and ensure the InputStream remains open during the response
            GridFSDownloadStream downloadStream = fsBucket.openDownloadStream(gridFsFile.getObjectId());
            InputStream inputStream = new BufferedInputStream(downloadStream);

            // Handle filename encoding for special characters
            String encodedFilename = encodeFilename(gridFsFile.getFilename());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF) // Set content type to PDF for browser to render it
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + encodedFilename + "\"") // Inline display (not download)
                    .body(new InputStreamResource(inputStream));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while processing the file ID: " + e.getMessage());
        }
    }

    // Method to handle filename encoding
    private String encodeFilename(String filename) {
        return URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }
}
