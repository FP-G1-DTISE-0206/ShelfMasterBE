package com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository;

import com.DTISE.ShelfMasterBE.entity.ProductStock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
    @Lock(LockModeType.OPTIMISTIC)
    Optional<ProductStock> findFirstByProductIdAndWarehouseId(Long productId, Long warehouseId);
}
