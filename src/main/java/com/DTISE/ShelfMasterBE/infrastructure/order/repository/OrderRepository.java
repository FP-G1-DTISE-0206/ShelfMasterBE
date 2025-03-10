package com.DTISE.ShelfMasterBE.infrastructure.order.repository;

import com.DTISE.ShelfMasterBE.entity.Order;
import com.DTISE.ShelfMasterBE.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
