package com.dda.website.model;

import com.dda.website.model.types.VideoFileProcessingTypes;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
    @JsonBackReference
    @ToString.Exclude
    private CustomerOrder customerOrder;

    public void setStatus(VideoFileProcessingTypes status) {
        this.status = status.toString();
    }
}
