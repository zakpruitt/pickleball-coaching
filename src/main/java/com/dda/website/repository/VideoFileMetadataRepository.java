package com.dda.website.repository;

import com.dda.website.model.VideoFileMetadata;
import com.dda.website.service.VideoFileMetadataService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoFileMetadataRepository extends JpaRepository<VideoFileMetadata, Long> {

    Optional<VideoFileMetadata> findByUuid(String Uuid);

    Boolean existsByUuid(String Uuid);

}
