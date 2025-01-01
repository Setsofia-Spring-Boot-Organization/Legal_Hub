package com.example.legal_hub.bundle;

import com.example.legal_hub.bundle.requests.UploadNewBundle;
import com.example.legal_hub.common.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "legal_hub/api/v1/bundles")
public class BundleController {

    private final BundleService bundleService;

    public BundleController(BundleService bundleService) {
        this.bundleService = bundleService;
    }

    @PostMapping(path = "/upload")
    public ResponseEntity<Response<?>> uploadBundle(
            @ModelAttribute UploadNewBundle newBundle,
            @RequestPart(name = "file") MultipartFile file
    ) throws IOException {
        return bundleService.uploadBundle(file, newBundle);
    }
}
