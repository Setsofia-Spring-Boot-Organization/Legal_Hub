package com.example.legal_hub.bundle;

import com.example.legal_hub.bundle.bundle_util.BundleUtil;
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
    private final BundleUtil bundleUtil;

    public BundleController(BundleService bundleService, BundleUtil bundleUtil) {
        this.bundleService = bundleService;
        this.bundleUtil = bundleUtil;
    }


    @PostMapping(path = "/upload")
    public ResponseEntity<Response<?>> uploadBundle(
            @ModelAttribute UploadNewBundle newBundle,
            @RequestPart(name = "file") MultipartFile file
    ) throws IOException {
        return bundleService.uploadBundle(file, newBundle);
    }


    @GetMapping()
    public ResponseEntity<Response<?>> getAllBundle() {
        return bundleService.getAllBundles();
    }


    @GetMapping(path = "/{bundleId}")
    public ResponseEntity<Response<?>> getBundle(@PathVariable String bundleId) {
        return bundleService.getBundle(bundleId);
    }


    @GetMapping("/viewBundle/{bundleId}")
    public ResponseEntity<?> downloadBundle(@PathVariable String bundleId) {
        return bundleUtil.downloadBundle(bundleId);
    }


    @PutMapping(path = "/update/{bundleId}")
    public ResponseEntity<Response<?>> updateBundle(
            @PathVariable String bundleId,
            @RequestPart(name = "file") MultipartFile file,
            @ModelAttribute UploadNewBundle bundle
    ) {
        return bundleService.updateBundle(bundleId, bundle, file);
    }
}
