package com.DTISE.ShelfMasterBE.infrastructure.order.repository;

import com.DTISE.ShelfMasterBE.entity.Order;
import com.DTISE.ShelfMasterBE.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN o.orderItems oi " +
            "WHERE o.user = :user " +
            "AND (:search IS NULL OR oi.product.name LIKE %:search%)")
    Page<Order> findByUserAndSearch(@Param("user") User user,
                           @Param("search") String search,
                           Pageable pageable);

    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN o.orderItems oi " +
            "WHERE :user MEMBER OF o.warehouse.admins " +
            "AND (:search IS NULL OR oi.product.name LIKE %:search%)")
    Page<Order> findByAdminAndSearch(@Param("user") User user,
                                     @Param("search") String search,
                                     Pageable pageable);

    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN o.orderItems oi " +
            "WHERE (:search IS NULL OR oi.product.name LIKE %:search%)")
    Page<Order> findBySearch(@Param("search") String search,
                                    Pageable pageable);

}
