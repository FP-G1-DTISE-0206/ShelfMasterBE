package com.DTISE.ShelfMasterBE.infrastructure.payment;

import com.DTISE.ShelfMasterBE.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
}
