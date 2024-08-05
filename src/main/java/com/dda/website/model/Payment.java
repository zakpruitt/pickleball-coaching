package com.dda.website.model;

import com.dda.website.model.types.PaymentProcessingTypes;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

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
    private String status;

    @OneToOne
    @JoinColumn(name = "customer_order_id", referencedColumnName = "id")
    @JsonBackReference
    @ToString.Exclude
    private CustomerOrder customerOrder;

    public void setStatus(PaymentProcessingTypes status) {
        this.status = status.toString();
    }
}
