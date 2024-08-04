package com.dda.website.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String note;
    private String packageSelected;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_metadata_id", referencedColumnName = "id")
    private VideoFileMetadata videoFileMetadata;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;

}
