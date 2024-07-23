package com.dda.website.service;

import com.dda.website.VideoFileProcessingTypes;
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
    private static final String FOLDER_ID = "1qdQsl5N6GGIIhZmo4F8iSZ2xs0kQJEwp";
    private static final Path TEMP_DIR = Paths.get("./src/main/resources/temp/");

    private final Drive drive;
    private final VideoFileMetadataService videoFileMetadataService;

    public String saveFileSynchronously(MultipartFile file, String uploadId) {
        try {
            if (!Files.exists(TEMP_DIR)) Files.createDirectories(TEMP_DIR);

            Path tempFilePath = TEMP_DIR.resolve(uploadId + "_" + file.getOriginalFilename());
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, tempFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return tempFilePath.toString();
        } catch (IOException e) {
            log.error("Error saving file synchronously!", e);
            throw new RuntimeException("Failed to save file", e);
        }
    }

    @Async
    public void uploadFileAsync(String filePath, VideoFileMetadata videoFileMetadata) {
        Path tempFilePath = Paths.get(filePath);
        java.io.File tempFile = tempFilePath.toFile();

        try {
            File fileMetadata = new File();
            fileMetadata.setName(tempFilePath.getFileName().toString());
            fileMetadata.setParents(Collections.singletonList(FOLDER_ID));

            FileContent mediaContent = new FileContent(Files.probeContentType(tempFilePath), tempFile);
            File uploadedFile = drive.files().create(fileMetadata, mediaContent)
                    .setFields("id, webContentLink, webViewLink")
                    .execute();
            if (!tempFile.delete()) {
                log.warn("Failed to delete temporary file: {}", tempFile.getAbsolutePath());
            }

            hydrateVideoFileMetadata(videoFileMetadata, uploadedFile.getWebViewLink(), VideoFileProcessingTypes.UPLOADED.toString());
            log.info("File uploaded to: {}", uploadedFile.getWebViewLink());
        } catch (Exception e) {
            log.error("Error during file upload", e);
            hydrateVideoFileMetadata(videoFileMetadata, null, "FAILED");
        }
    }

    private void hydrateVideoFileMetadata(VideoFileMetadata videoFileMetadata, String fileUrl, String status) {
        videoFileMetadata.setFileUrl(fileUrl);
        videoFileMetadata.setStatus(status);
        videoFileMetadataService.updateVideoFileMetadata(videoFileMetadata);
    }

}
