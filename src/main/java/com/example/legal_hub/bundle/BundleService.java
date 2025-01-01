package com.example.legal_hub.bundle;

import com.example.legal_hub.bundle.requests.UploadNewBundle;
import com.example.legal_hub.common.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BundleService {

    /**
     * This method handles the uploading of a bundle file.
     *
     * @param file the {@link MultipartFile} object representing the file to be uploaded
     * @return a {@link ResponseEntity} containing a {@link Response} with the status and details of the upload operation
     * @throws IOException if an error occurs while processing the file
     */
    ResponseEntity<Response<?>> uploadBundle(MultipartFile file, UploadNewBundle newBundle) throws IOException;
}
