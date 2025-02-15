package com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository;

import com.DTISE.ShelfMasterBE.entity.ProductMutation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMutationRepository extends JpaRepository<ProductMutation, Long> {
    Page<ProductMutation> getAllByWarehouseId(Pageable pageable, Long warehouseId);
}
