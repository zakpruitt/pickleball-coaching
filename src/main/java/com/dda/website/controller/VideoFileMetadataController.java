package com.dda.website.controller;

import com.dda.website.VideoFileProcessingTypes;
import com.dda.website.model.CustomerOrder;
import com.dda.website.model.VideoFileMetadata;
import com.dda.website.service.CustomerOrderService;
import com.dda.website.service.GoogleDriveService;
import com.dda.website.service.VideoFileMetadataService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/videos")
@AllArgsConstructor
@Slf4j
public class VideoFileMetadataController {
    private final VideoFileMetadataService videoFileMetadataService;
    private final CustomerOrderService customerOrderService;
    private final GoogleDriveService googleDriveService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file,
                                                          @RequestParam("customerOrderId") Long customerOrderId) {
        VideoFileMetadata videoFileMetadata = VideoFileMetadata.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .status(VideoFileProcessingTypes.IN_PROGRESS.toString())
                .build();
        videoFileMetadata = videoFileMetadataService.saveVideoFileMetadata(videoFileMetadata);
        CustomerOrder customerOrder = customerOrderService.setVideoFileMetadataId(videoFileMetadata, customerOrderId);

        // Save the file synchronously
        String tempFilePath = googleDriveService.saveFileSynchronously(file, videoFileMetadata.getId());

        // Trigger asynchronous upload
        googleDriveService.uploadFileAsync(tempFilePath, videoFileMetadata.getId(), customerOrder);

        Map<String, String> response = new HashMap<>();
        response.put("uploadId", videoFileMetadata.getId());
        return ResponseEntity.ok(response);
    }
}
