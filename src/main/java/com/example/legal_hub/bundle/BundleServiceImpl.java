package com.example.legal_hub.bundle;

import com.example.legal_hub.bundle.bundle_util.BundleUtil;
import com.example.legal_hub.bundle.requests.UploadNewBundle;
import com.example.legal_hub.bundle.responses.BundleData;
import com.example.legal_hub.common.Response;
import com.example.legal_hub.exceptions.Error;
import com.example.legal_hub.exceptions.LegalHubException;
import com.example.legal_hub.exceptions.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class BundleServiceImpl implements BundleService{

    private final BundleUtil bundleUtil;
    private final BundleRepository bundleRepository;

    public BundleServiceImpl(BundleUtil bundleUtil, BundleRepository bundleRepository) {
        this.bundleUtil = bundleUtil;
        this.bundleRepository = bundleRepository;
    }


    @Override
    public ResponseEntity<Response<?>> getBundle(String bundleId) {

        AtomicReference<BundleData> dataAtomicReference = new AtomicReference<>();

        bundleRepository.findById(bundleId).ifPresent(bundle -> dataAtomicReference.set(
                new BundleData(
                        bundle.getId(),
                        bundle.getCreatedAt(),
                        bundle.getUpdatedAt(),
                        bundle.getAuthor(),
                        bundle.getTitle(),
                        bundle.getDescription(),
                        bundle.getCategory(),
                        "/legal_hub/api/v1/bundles/viewBundle/" + bundle.getBundle() // Add file download URL
                )
        ));

        Response<?> response = new Response.Builder<>()
                .status(HttpStatus.OK.value())
                .message("bundles")
                .data(dataAtomicReference)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @Override
    public ResponseEntity<Response<?>> uploadBundle(MultipartFile file, UploadNewBundle newBundle) throws IOException {

        //upload the pdf file and retrieve the GidFS file id
        String bundleId = bundleUtil.uploadBundle(file);

        //create the new bundle
        Bundle bundle = new Bundle(
                LocalDateTime.now(),
                LocalDateTime.now(),
                newBundle.author(),
                newBundle.title(),
                newBundle.description(),
                newBundle.category(),
                bundleId
        );

        try {
            Bundle savedBundle = bundleRepository.save(bundle);

            return ResponseEntity.status(HttpStatus.OK).body(new Response.Builder<>()
                    .status(HttpStatus.OK.value())
                    .message("bundle uploaded successfully")
                    .data(savedBundle)
                    .build()
            );
        } catch (Exception e) {
            throw new LegalHubException(Error.ERROR_SAVING_DATA, new Throwable(Message.THE_DATA_CANNOT_BE_SAVED_PLEASE_TRY_AGAIN.label));
        }
    }



    @Override
    public ResponseEntity<Response<?>> getAllBundles() {

        List<BundleData> bundles = new ArrayList<>();

        //get the bundle
        bundleRepository.findAll().forEach(bundle -> bundles.add(
                new BundleData(
                        bundle.getId(),
                        bundle.getCreatedAt(),
                        bundle.getUpdatedAt(),
                        bundle.getAuthor(),
                        bundle.getTitle(),
                        bundle.getDescription(),
                        bundle.getCategory(),
                        "/legal_hub/api/v1/bundles/viewBundle/" + bundle.getBundle() // Add file download URL
                )
        ));

        Response<?> response = new Response.Builder<>()
                .status(HttpStatus.OK.value())
                .message("bundles")
                .data(bundles)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @Override
    public ResponseEntity<Response<?>> updateBundle(String bundleId, UploadNewBundle updateBundle, MultipartFile file) {

        bundleRepository.findById(bundleId).ifPresent(
                bundle -> {
                    bundle.setUpdatedAt(LocalDateTime.now());
                    bundle.setAuthor(updateBundle.author() == null || updateBundle.author().isEmpty()? bundle.getAuthor(): updateBundle.author());
                    bundle.setTitle(updateBundle.title() == null || updateBundle.title().isEmpty()? bundle.getTitle(): updateBundle.title());
                    bundle.setCategory(updateBundle.category() == null || updateBundle.category().isEmpty()? bundle.getCategory(): updateBundle.category());
                    bundle.setDescription(updateBundle.description() == null || updateBundle.description().isEmpty()? bundle.getDescription(): updateBundle.description());
                    try {
                        bundle.setBundle(file == null || file.isEmpty()? bundle.getBundle(): bundleUtil.uploadBundle(file));
                    } catch (IOException e) {
                        throw new LegalHubException(Error.ERROR_UPDATING_DATA, new Throwable(Message.UPDATE_FAILED_TRY_AGAIN_LATER.label));
                    }

                    //save the updated bundle
                    bundleRepository.save(bundle);
                }
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response.Builder<>()
                .status(HttpStatus.NO_CONTENT.value())
                .message("bundle updated successfully")
                .build());
    }
}
