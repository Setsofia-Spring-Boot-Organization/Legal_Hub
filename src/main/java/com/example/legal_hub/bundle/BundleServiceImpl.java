package com.example.legal_hub.bundle;

import com.example.legal_hub.bundle.bundle_util.BundleUtil;
import com.example.legal_hub.common.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class BundleServiceImpl implements BundleService{

    private final BundleUtil bundleUtil;

    public BundleServiceImpl(BundleUtil bundleUtil) {
        this.bundleUtil = bundleUtil;
    }

    @Override
    public ResponseEntity<Response<?>> uploadBundle(MultipartFile file) throws IOException {
        String id = bundleUtil.uploadBundle(file);

        return ResponseEntity.status(HttpStatus.OK).body(new Response.Builder<>()
                .status(HttpStatus.OK.value())
                .message("bundle uploaded successfully")
                .data(id)
                .build()
        );
    }
}
