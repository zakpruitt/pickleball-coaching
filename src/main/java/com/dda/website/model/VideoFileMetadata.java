package com.dda.website.model;

import com.dda.website.model.types.VideoFileProcessingTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class VideoFileMetadata {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private String fileName;
    private String fileType;
    private String fileUrl;
    private String status;

    @OneToOne
    @JoinColumn(name = "customer_order_id", referencedColumnName = "id")
    private CustomerOrder customerOrder;

    public void setStatus(VideoFileProcessingTypes status) {
        this.status = status.toString();
    }

}
