package com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository;

import com.DTISE.ShelfMasterBE.entity.ProductMutationOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductMutationOrderRepository extends JpaRepository<ProductMutationOrder, Long> {
    List<ProductMutationOrder> findAllByOrderIdOrderByIdDesc(Long orderId);
}
