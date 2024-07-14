package com.dda.website.service;

import com.dda.website.model.CustomerOrder;
import com.dda.website.model.VideoFileMetadata;
import com.dda.website.repository.CustomerOrderRepository;
import com.dda.website.repository.VideoFileMetadataRepository;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

@Service
@AllArgsConstructor
@Slf4j
public class GoogleDriveService {
    private final String FOLDER_ID = "1qdQsl5N6GGIIhZmo4F8iSZ2xs0kQJEwp";

    private Drive drive;
    private VideoFileMetadataRepository videoFileMetadataRepository;
    private CustomerOrderRepository customerOrderRepository;

    @Async
    public void uploadFileAsync(MultipartFile file, String uploadId) {
        try {
            // create Google Drive file
            File fileMetadata = new File();
            fileMetadata.setName(uploadId + "_" + file.getOriginalFilename());
            fileMetadata.setParents(Collections.singletonList(FOLDER_ID));

            // create temporary file
            java.io.File tempFile = java.io.File.createTempFile("temp", file.getOriginalFilename());
            file.transferTo(tempFile);

            FileContent mediaContent = new FileContent(file.getContentType(), tempFile);
            File uploadedFile = drive.files().create(fileMetadata, mediaContent)
                    .setFields("id, webContentLink, webViewLink")
                    .execute();
            if (tempFile.exists()) tempFile.delete();

            VideoFileMetadata metadata = videoFileMetadataRepository.findById(uploadId).orElseThrow(() -> new RuntimeException("Metadata not found"));
            metadata.setFileUrl(uploadedFile.getWebViewLink());
            metadata.setStatus("COMPLETED");
            videoFileMetadataRepository.save(metadata);

            // Update customer order with file metadata
            CustomerOrder customerOrder = customerOrderRepository.findByFileMetadataId(uploadId);
            if (customerOrder != null) {
                customerOrder.setFileMetadata(metadata);
                customerOrderRepository.save(customerOrder);
            }

            // Log upload success and time taken
            log.info("Finished file upload at " + System.currentTimeMillis());
            log.info("File uploaded to: " + uploadedFile.getWebViewLink());
        } catch (Exception e) {
            log.error("Error during file upload", e);

            // Update metadata with failure status
            VideoFileMetadata metadata = videoFileMetadataRepository.findById(uploadId).orElseThrow(() -> new RuntimeException("Metadata not found"));
            metadata.setStatus("FAILED");
            videoFileMetadataRepository.save(metadata);
        }
    }
}
