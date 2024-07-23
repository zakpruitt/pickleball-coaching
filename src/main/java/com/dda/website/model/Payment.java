package com.dda.website.model;

import com.dda.website.model.types.PaymentProcessingTypes;
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
    private String status;

    @OneToOne
    @JoinColumn(name = "customer_order_id", referencedColumnName = "id")
    private CustomerOrder customerOrder;

    public void setStatus(PaymentProcessingTypes status) {
        this.status = status.toString();
    }

}
