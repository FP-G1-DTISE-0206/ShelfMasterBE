package com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository;

import com.DTISE.ShelfMasterBE.entity.ProductStock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
    @Lock(LockModeType.OPTIMISTIC)
    Optional<ProductStock> findFirstByProductIdAndWarehouseId(Long productId, Long warehouseId);

    @Query("""
       SELECT SUM(ps.quantity)
       FROM ProductStock ps
       WHERE ps.productId = :productId
    """)
    Long getTotalStockByProductId(@Param("productId") Long productId);

    @Query("""
       SELECT ps
       FROM ProductStock ps
       INNER JOIN Warehouse w on ps.warehouseId = w.id
       WHERE ps.productId = :productId AND ps.quantity > 0
       ORDER BY
         (6371 * acos(cos(radians((SELECT w2.latitude FROM
           Warehouse w2 WHERE w2.id = :warehouseId))) * cos(radians(w.latitude))
           * cos(radians(w.longitude) - radians((SELECT w2.longitude FROM Warehouse w2
           WHERE w2.id = :warehouseId))) + sin(radians((SELECT w2.longitude FROM Warehouse w2
           WHERE w2.id = :warehouseId))) * sin(radians(w.latitude))))
    """)
    Page<ProductStock> getStockByProductIdOrderByLocation(
            Pageable pageable,
            @Param("productId") Long productId,
            @Param("warehouseId") Long warehouseId
    );
}
