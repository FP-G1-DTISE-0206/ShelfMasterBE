package com.DTISE.ShelfMasterBE.infrastructure.payment.repository;

import com.DTISE.ShelfMasterBE.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(Long orderId);
    Optional<Payment> findByOrderIdAndTransactionStatus(Long orderId, String status);
}
