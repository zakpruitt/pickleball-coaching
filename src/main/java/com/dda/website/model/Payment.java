package com.dda.website.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stripePaymentId;
    @Column(length = 1000)
    private String stripePaymentUrl;
    private Long currencyAmount;
    private String currencyType;
    private String paymentStatus;

    @OneToOne
    @JoinColumn(name = "order_id")
    private CustomerOrder customerOrder;
}
