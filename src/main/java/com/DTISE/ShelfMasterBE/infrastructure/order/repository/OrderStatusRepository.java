package com.DTISE.ShelfMasterBE.infrastructure.order.repository;

import com.DTISE.ShelfMasterBE.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
}
