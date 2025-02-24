package com.DTISE.ShelfMasterBE.infrastructure.order.repository;

import com.DTISE.ShelfMasterBE.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrderId(Long OrderId);
}
