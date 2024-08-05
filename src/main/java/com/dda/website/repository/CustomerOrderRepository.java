package com.dda.website.repository;

import com.dda.website.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    @Query("SELECT o FROM CustomerOrder o " +
            "WHERE o.emailSent = false " +
            "AND o.payment.status = 'COMPLETED' " +
            "AND o.videoFileMetadata.status = 'UPLOADED'")
    List<CustomerOrder> findPendingOrderEmails();
}
