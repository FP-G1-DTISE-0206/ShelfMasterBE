package com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository;

import com.DTISE.ShelfMasterBE.entity.ProductMutation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

public interface ProductMutationRepository extends JpaRepository<ProductMutation, Long> {
    Page<ProductMutation> getAllByWarehouseId(Pageable pageable, Long warehouseId);

    @Modifying
    @Query("""
        UPDATE ProductMutation pm SET pm.processedBy = :userId, pm.updatedAt = :newUpdatedAt
        WHERE pm.id = :id AND pm.updatedAt = :oldUpdatedAt
    """)
    int cancelOrRejectProductMutationWithOptimisticLocking(
            @Param("id") Long id,
            @Param("userId") Long userId,
            @Param("oldUpdatedAt") OffsetDateTime oldUpdatedAt,
            @Param("newUpdatedAt") OffsetDateTime newUpdatedAt);

    @Modifying
    @Query("""
        UPDATE ProductMutation pm SET pm.processedBy = :userId, pm.updatedAt = :newUpdatedAt,
        pm.isApproved = true
        WHERE pm.id = :id AND pm.updatedAt = :oldUpdatedAt
    """)
    int approveProductMutationWithOptimisticLocking(
            @Param("id") Long id,
            @Param("userId") Long userId,
            @Param("oldUpdatedAt") OffsetDateTime oldUpdatedAt,
            @Param("newUpdatedAt") OffsetDateTime newUpdatedAt);
}
