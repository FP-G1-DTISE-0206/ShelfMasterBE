package com.DTISE.ShelfMasterBE.infrastructure.report.repository;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import com.DTISE.ShelfMasterBE.entity.ProductMutation;
import com.DTISE.ShelfMasterBE.infrastructure.report.dto.StockReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface ProductMutationRepository1 extends JpaRepository<ProductMutation, Long> {
    @Query("""
       SELECT new com.DTISE.ShelfMasterBE.infrastructure.report.dto.StockReportResponse(
           pm.id,
           CASE
               WHEN pm.mutationType.originType = :warehouseEnum THEN coalesce(wo.name, '')
               WHEN pm.mutationType.originType = :vendorEnum THEN coalesce(vo.name, '')
               WHEN pm.mutationType.originType = :userEnum THEN coalesce(uo.userName, '')
               ELSE ''
           END,
           CASE
               WHEN pm.mutationType.destinationType = :warehouseEnum THEN coalesce(wd.name, '')
               WHEN pm.mutationType.destinationType = :vendorEnum THEN coalesce(vd.name, '')
               WHEN pm.mutationType.destinationType = :userEnum THEN coalesce(ud.userName, '')
               ELSE ''
           END,
           pm.product.id,
           pm.product.name,
           CASE
               WHEN pm.mutationType.originType = :warehouseEnum AND pm.mutationType.destinationType = :warehouseEnum AND coalesce(pmor.id, 0) <> 0 THEN pm.quantity
               WHEN pm.mutationType.originType = :warehouseEnum AND pm.mutationType.destinationType = :warehouseEnum AND coalesce(pmo.id, 0) <> 0 THEN (-1 * pm.quantity)
               WHEN pm.mutationType.originType = :warehouseEnum AND pm.mutationType.destinationType = :warehouseEnum THEN 0
               WHEN pm.mutationType.originType = :userEnum THEN pm.quantity
               WHEN pm.mutationType.destinationType = :userEnum THEN (-1 * pm.quantity)
               WHEN pm.mutationType.originType = :vendorEnum THEN pm.quantity
               ELSE 0
           END,
           pm.quantity,
           pm.requestedByUser.userName,
           COALESCE(pm.processedByUser.userName, ''),
           CASE
               WHEN pm.mutationType.originType = :warehouseEnum AND pm.mutationType.destinationType = :warehouseEnum AND coalesce(pmor.id, 0) <> 0 THEN 'internal order return'
               WHEN pm.mutationType.originType = :warehouseEnum AND pm.mutationType.destinationType = :warehouseEnum AND coalesce(pmo.id, 0) <> 0 THEN 'internal order'
               WHEN pm.mutationType.originType = :warehouseEnum AND pm.mutationType.destinationType = :warehouseEnum THEN 'internal mutation'
               WHEN pm.mutationType.originType = :userEnum THEN 'order return'
               WHEN pm.mutationType.destinationType = :userEnum THEN 'order'
               WHEN pm.mutationType.originType = :vendorEnum THEN 'restock'
               ELSE 'unknown'
           END
       )
       FROM ProductMutation pm
           LEFT JOIN ProductMutationOrder pmo ON pm.id = pmo.orderedProductMutationId
           LEFT JOIN ProductMutationOrder pmor ON pm.id = pmor.returnedProductMutationId
           LEFT JOIN User uo ON pm.originId = uo.id
           LEFT JOIN User ud ON pm.destinationId = ud.id
           LEFT JOIN Vendor vo ON pm.originId = vo.id
           LEFT JOIN Vendor vd ON pm.destinationId = vd.id
           LEFT JOIN Warehouse wo ON pm.originId = wo.id
           LEFT JOIN Warehouse wd ON pm.destinationId = wd.id
       WHERE pm.isApproved = true AND (:productId IS NULL OR pm.product.id = :productId)
           AND pm.updatedAt >= :startDate
           AND pm.updatedAt <= :endDate
           AND (:warehouseId IS NULL
               OR (pm.mutationType.destinationType = :warehouseEnum AND :warehouseId = pm.destinationId)
               OR (pm.mutationType.originType = :warehouseEnum AND :warehouseId = pm.originId))
    """)
    Page<StockReportResponse> getSalesReportPreview(
            Pageable pageable,
            @Param("userEnum") MutationEntityType userEnum,
            @Param("vendorEnum") MutationEntityType vendorEnum,
            @Param("warehouseEnum") MutationEntityType warehouseEnum,
            @Param("productId") Long productId,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate,
            @Param("warehouseId") Long warehouseId);

    @Query("""
       SELECT new com.DTISE.ShelfMasterBE.infrastructure.report.dto.StockReportResponse(
           pm.id,
           CASE
               WHEN pm.mutationType.originType = :warehouseEnum THEN coalesce(wo.name, '')
               WHEN pm.mutationType.originType = :vendorEnum THEN coalesce(vo.name, '')
               WHEN pm.mutationType.originType = :userEnum THEN coalesce(uo.userName, '')
               ELSE ''
           END,
           CASE
               WHEN pm.mutationType.destinationType = :warehouseEnum THEN coalesce(wd.name, '')
               WHEN pm.mutationType.destinationType = :vendorEnum THEN coalesce(vd.name, '')
               WHEN pm.mutationType.destinationType = :userEnum THEN coalesce(ud.userName, '')
               ELSE ''
           END,
           pm.product.id,
           pm.product.name,
           CASE
               WHEN pm.mutationType.originType = :warehouseEnum AND pm.mutationType.destinationType = :warehouseEnum AND coalesce(pmor.id, 0) <> 0 THEN pm.quantity
               WHEN pm.mutationType.originType = :warehouseEnum AND pm.mutationType.destinationType = :warehouseEnum AND coalesce(pmo.id, 0) <> 0 THEN (-1 * pm.quantity)
               WHEN pm.mutationType.originType = :warehouseEnum AND pm.mutationType.destinationType = :warehouseEnum THEN 0
               WHEN pm.mutationType.originType = :userEnum THEN pm.quantity
               WHEN pm.mutationType.destinationType = :userEnum THEN (-1 * pm.quantity)
               WHEN pm.mutationType.originType = :vendorEnum THEN pm.quantity
               ELSE 0
           END,
           pm.quantity,
           pm.requestedByUser.userName,
           COALESCE(pm.processedByUser.userName, ''),
           CASE
               WHEN pm.mutationType.originType = :warehouseEnum AND pm.mutationType.destinationType = :warehouseEnum AND coalesce(pmor.id, 0) <> 0 THEN 'internal order return'
               WHEN pm.mutationType.originType = :warehouseEnum AND pm.mutationType.destinationType = :warehouseEnum AND coalesce(pmo.id, 0) <> 0 THEN 'internal order'
               WHEN pm.mutationType.originType = :warehouseEnum AND pm.mutationType.destinationType = :warehouseEnum THEN 'internal mutation'
               WHEN pm.mutationType.originType = :userEnum THEN 'order return'
               WHEN pm.mutationType.destinationType = :userEnum THEN 'order'
               WHEN pm.mutationType.originType = :vendorEnum THEN 'restock'
               ELSE 'unknown'
           END
       )
       FROM ProductMutation pm
           LEFT JOIN ProductMutationOrder pmo ON pm.id = pmo.orderedProductMutationId
           LEFT JOIN ProductMutationOrder pmor ON pm.id = pmor.returnedProductMutationId
           LEFT JOIN User uo ON pm.originId = uo.id
           LEFT JOIN User ud ON pm.destinationId = ud.id
           LEFT JOIN Vendor vo ON pm.originId = vo.id
           LEFT JOIN Vendor vd ON pm.destinationId = vd.id
           LEFT JOIN Warehouse wo ON pm.originId = wo.id
           LEFT JOIN Warehouse wd ON pm.destinationId = wd.id
       WHERE pm.isApproved = true AND (:productId IS NULL OR pm.product.id = :productId)
           AND pm.updatedAt >= :startDate
           AND pm.updatedAt <= :endDate
           AND (:warehouseId IS NULL
               OR (pm.mutationType.destinationType = :warehouseEnum AND :warehouseId = pm.destinationId)
               OR (pm.mutationType.originType = :warehouseEnum AND :warehouseId = pm.originId))
    """)
    List<StockReportResponse> getSalesReport(
            @Param("userEnum") MutationEntityType userEnum,
            @Param("vendorEnum") MutationEntityType vendorEnum,
            @Param("warehouseEnum") MutationEntityType warehouseEnum,
            @Param("productId") Long productId,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate,
            @Param("warehouseId") Long warehouseId);
}
