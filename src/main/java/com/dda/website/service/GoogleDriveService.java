package com.dda.website.service;

import com.dda.website.VideoFileProcessingTypes;
import com.dda.website.model.CustomerOrder;
import com.dda.website.model.VideoFileMetadata;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;

@Service
@AllArgsConstructor
@Slf4j
public class GoogleDriveService {
    private final String FOLDER_ID = "1qdQsl5N6GGIIhZmo4F8iSZ2xs0kQJEwp";

    private final Drive drive;
    private final VideoFileMetadataService videoFileMetadataService;
    private final CustomerOrderService customerOrderService;

    public String saveFileSynchronously(MultipartFile file, String uploadId) {
        Path tempDir = Paths.get("./src/main/resources/temp/");
        try {
            // Ensure your custom temporary directory exists
            if (!Files.exists(tempDir)) {
                Files.createDirectories(tempDir);
            }

            // Create a temporary file in your custom directory
            Path tempFilePath = tempDir.resolve(uploadId + "_" + file.getOriginalFilename());

            // Copy the content of the uploaded file to the temporary file
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, tempFilePath, StandardCopyOption.REPLACE_EXISTING);
            }

            return tempFilePath.toString();
        } catch (IOException e) {
            log.error("Error saving file synchronously", e);
            throw new RuntimeException("Failed to save file", e);
        }
    }

    @Async
    public void uploadFileAsync(String filePath, String uploadId, CustomerOrder customerOrder) {
        Path tempFilePath = Paths.get(filePath);
        try {
            // Create Google Drive file metadata
            File fileMetadata = new File();
            fileMetadata.setName(tempFilePath.getFileName().toString());
            fileMetadata.setParents(Collections.singletonList(FOLDER_ID));

            // Create FileContent for Google Drive upload
            java.io.File tempFile = tempFilePath.toFile();
            FileContent mediaContent = new FileContent(Files.probeContentType(tempFilePath), tempFile);
            File uploadedFile = drive.files().create(fileMetadata, mediaContent)
                    .setFields("id, webContentLink, webViewLink")
                    .execute();

            // Clean up the temporary file
            if (tempFile.exists()) {
                if (!tempFile.delete()) {
                    log.warn("Failed to delete temporary file: " + tempFile.getAbsolutePath());
                }
            }

            // Update metadata and save
            VideoFileMetadata metadata = videoFileMetadataService.findVideoFileMetadataById(uploadId).orElseThrow(
                    () -> new RuntimeException("Metadata not found")
            );
            metadata.setFileUrl(uploadedFile.getWebViewLink());
            metadata.setStatus(VideoFileProcessingTypes.UPLOADED.toString());
            videoFileMetadataService.saveVideoFileMetadata(metadata);
            customerOrderService.setVideoFileMetadataId(metadata, customerOrder);

            // Log upload success and time taken
            log.info("Finished file upload at " + System.currentTimeMillis());
            log.info("File uploaded to: " + uploadedFile.getWebViewLink());
        } catch (Exception e) {
            log.error("Error during file upload", e);

            // Update metadata with failure status
            VideoFileMetadata metadata = videoFileMetadataService.findVideoFileMetadataById(uploadId).orElseThrow(
                    () -> new RuntimeException("Metadata not found")
            );
            metadata.setStatus("FAILED");
            videoFileMetadataService.saveVideoFileMetadata(metadata);
        }
    }
}
