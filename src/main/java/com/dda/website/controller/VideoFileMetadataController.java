package com.dda.website.controller;

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

@RestController
@RequestMapping("/api/videos")
@AllArgsConstructor
@Slf4j
public class VideoFileMetadataController {
    private final VideoFileMetadataService videoFileMetadataService;
    private final CustomerOrderService customerOrderService;
    private final GoogleDriveService googleDriveService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("customerOrderId") Long customerOrderId) {
        CustomerOrder customerOrder = customerOrderService.findOrderById(customerOrderId);
        VideoFileMetadata videoFileMetadata = videoFileMetadataService.createVideoFileMetadata(file, customerOrder);
        customerOrderService.setVideoFileMetadata(customerOrder, videoFileMetadata);

        String tempFilePath = googleDriveService.saveFileSynchronously(file, videoFileMetadata.getId());
        googleDriveService.uploadFileAsync(tempFilePath, videoFileMetadata);

        return ResponseEntity.ok(videoFileMetadata.getId());
    }


}
