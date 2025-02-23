package com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository;

import com.DTISE.ShelfMasterBE.entity.ProductMutationOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductMutationOrderRepository extends JpaRepository<ProductMutationOrderRepository, Long> {
    List<ProductMutationOrder> findAllByOrderId(Long orderId);
}
