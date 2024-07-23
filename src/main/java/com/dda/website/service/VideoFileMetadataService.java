package com.dda.website.service;

import com.dda.website.VideoFileProcessingTypes;
import com.dda.website.model.CustomerOrder;
import com.dda.website.model.VideoFileMetadata;
import com.dda.website.repository.VideoFileMetadataRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class VideoFileMetadataService {

    private VideoFileMetadataRepository videoFileMetadataRepository;

    public VideoFileMetadata createVideoFileMetadata(MultipartFile file, CustomerOrder customerOrder) {
        VideoFileMetadata videoFileMetadata = VideoFileMetadata.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .status(VideoFileProcessingTypes.IN_PROGRESS.toString())
                .customerOrder(customerOrder)
                .build();
        return videoFileMetadataRepository.save(videoFileMetadata);
    }

    public VideoFileMetadata updateVideoFileMetadata(VideoFileMetadata videoFileMetadata) {
        return videoFileMetadataRepository.save(videoFileMetadata);
    }

    public VideoFileMetadata findVideoFileMetadataById(String uuid) {
        return videoFileMetadataRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Video File Metadata not found!"));
    }

}
