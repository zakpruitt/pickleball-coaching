package com.dda.website.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class VideoFileMetadata {

    @Id
    private String id;
    private String uuid;
    private String fileName;
    private String fileType;
    private String fileUrl;
    private String status;

    @OneToOne(mappedBy = "fileMetadata")
    private CustomerOrder customerOrder;

}
