package com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import com.DTISE.ShelfMasterBE.entity.ProductMutation;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.ProductMutationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

public interface ProductMutationRepository extends JpaRepository<ProductMutation, Long> {
    @Query("""
        SELECT pm FROM ProductMutation pm
        WHERE (:search IS NULL OR :search = ''
               OR LOWER(pm.product.name) LIKE LOWER(CONCAT('%', :search, '%')))
        AND EXISTS (
            SELECT 1 FROM MutationType mt
            WHERE (mt.originType = :warehouse OR mt.destinationType = :warehouse)
            AND mt = pm.mutationType
        )
        AND (pm.originId = :warehouseId OR pm.destinationId = :warehouseId)
    """)
    Page<ProductMutation> getAllBySearchAndWarehouseId(
            @Param("search") String search,
            Pageable pageable,
            @Param("warehouseId") Long warehouseId,
            @Param("warehouse") MutationEntityType warehouse);


    @Modifying
    @Query("""
        UPDATE ProductMutation pm SET pm.processedByUser.id = :userId, pm.updatedAt = :newUpdatedAt
        WHERE pm.id = :id AND pm.updatedAt = :oldUpdatedAt
    """)
    int cancelOrRejectProductMutationWithOptimisticLocking(
            @Param("id") Long id,
            @Param("userId") Long userId,
            @Param("oldUpdatedAt") OffsetDateTime oldUpdatedAt,
            @Param("newUpdatedAt") OffsetDateTime newUpdatedAt);

    @Modifying
    @Query("""
        UPDATE ProductMutation pm SET pm.processedByUser.id = :userId, pm.updatedAt = :newUpdatedAt,
        pm.isApproved = true
        WHERE pm.id = :id AND pm.updatedAt = :oldUpdatedAt
    """)
    int approveProductMutationWithOptimisticLocking(
            @Param("id") Long id,
            @Param("userId") Long userId,
            @Param("oldUpdatedAt") OffsetDateTime oldUpdatedAt,
            @Param("newUpdatedAt") OffsetDateTime newUpdatedAt);
}
