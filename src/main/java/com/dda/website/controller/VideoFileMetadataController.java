package com.dda.website.controller;

import com.dda.website.model.CustomerOrder;
import com.dda.website.model.VideoFileMetadata;
import com.dda.website.repository.VideoFileMetadataRepository;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/videos")
@AllArgsConstructor
@Slf4j
public class VideoFileMetadataController {

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("customerOrderId") Long customerOrderId) {
        long startTime = System.currentTimeMillis();
        log.info("Starting file upload at " + startTime);

        // Store metadata
        VideoFileMetadata metadata = new VideoFileMetadata();
        metadata.setId(uploadId);
        metadata.setFileName(file.getOriginalFilename());
        metadata.setFileType(file.getContentType());
        metadata.setStatus("IN_PROGRESS");
        VideoFileMetadataRepository.save(metadata);

        // Associate file metadata with customer order
        CustomerOrder customerOrder = customerOrderRepository.findById(customerOrderId).orElseThrow(() -> new RuntimeException("CustomerOrder not found"));
        customerOrder.setFileMetadata(metadata);
        customerOrderRepository.save(customerOrder);

        // Call the async method to upload the file
        googleDriveService.uploadFileAsync(file, uploadId);

        long endTime = System.currentTimeMillis();
        log.info("Returning response at " + endTime);
        log.info("Time taken to initiate upload: " + (endTime - startTime) + " ms");

        // Return the unique ID to the client
        Map<String, String> response = new HashMap<>();
        response.put("uploadId", uploadId);
        return ResponseEntity.ok(response);
    }

}
