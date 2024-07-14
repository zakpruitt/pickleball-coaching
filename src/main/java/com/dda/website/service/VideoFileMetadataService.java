package com.dda.website.service;

import com.dda.website.model.CustomerOrder;
import com.dda.website.model.VideoFileMetadata;
import com.dda.website.repository.VideoFileMetadataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class VideoFileMetadataService {

    private VideoFileMetadataRepository videoFileMetadataRepository;

    public VideoFileMetadata saveVideoFileMetadata(VideoFileMetadata videoFileMetadata) {
        return videoFileMetadataRepository.save(videoFileMetadata);
    }

    public Optional<VideoFileMetadata> findVideoFileMetadataByUUID(String uuid) {
        return videoFileMetadataRepository.findByUuid(uuid);
    }

    public String getUniqueUUID() {
        String generatedUUID;
        do {
            generatedUUID = UUID.randomUUID().toString();
        } while (videoFileMetadataRepository.existsByUuid(generatedUUID));
        return generatedUUID;
    }

}
