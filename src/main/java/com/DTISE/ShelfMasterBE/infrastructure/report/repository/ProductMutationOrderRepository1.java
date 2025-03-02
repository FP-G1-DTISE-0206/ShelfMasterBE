package com.DTISE.ShelfMasterBE.infrastructure.report.repository;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import com.DTISE.ShelfMasterBE.entity.ProductMutationOrder;
import com.DTISE.ShelfMasterBE.infrastructure.report.dto.GraphResponse;
import com.DTISE.ShelfMasterBE.infrastructure.report.dto.PopularProductResponse;
import com.DTISE.ShelfMasterBE.infrastructure.report.dto.SalesInfoCardResponse;
import com.DTISE.ShelfMasterBE.infrastructure.report.dto.SalesReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface ProductMutationOrderRepository1 extends JpaRepository<ProductMutationOrder, Long> {
    @Query("""
        SELECT new com.DTISE.ShelfMasterBE.infrastructure.report.dto.SalesInfoCardResponse(
           SUM(CASE
                   WHEN oi.createdAt >= :startOfThisWeek THEN oi.totalPrice ELSE 0
               END),
           CASE
               WHEN SUM(CASE
                   WHEN oi.createdAt >= :startOfLastWeek
                       AND oi.createdAt < :startOfThisWeek THEN oi.totalPrice ELSE 0
                   END) = 0 THEN NULL
               ELSE (SUM(CASE WHEN oi.createdAt >= :startOfThisWeek
                           THEN oi.totalPrice ELSE 0 END)
                      - SUM(CASE WHEN oi.createdAt >= :startOfLastWeek
                           AND oi.createdAt < :startOfThisWeek THEN oi.totalPrice ELSE 0 END))
                      * 100
                      / SUM(CASE
                           WHEN oi.createdAt >= :startOfLastWeek
                               AND oi.createdAt < :startOfThisWeek THEN oi.totalPrice ELSE 0
                           END)
           END
       )
       FROM ProductMutationOrder pmo
           JOIN ProductMutation pm ON pm.id = pmo.orderedProductMutationId
           JOIN OrderItem oi ON oi.orderId = pmo.orderId
       WHERE pmo.returnedProductMutationId IS NULL
           AND (:warehouseId IS NULL
               OR (pm.mutationType.originType = :originType AND pm.originId = :warehouseId))
           AND pm.mutationType.destinationType = :destinationType
    """)
    SalesInfoCardResponse getTotalSalesThisWeek(
            @Param("originType") MutationEntityType originType,
            @Param("destinationType") MutationEntityType destinationType,
            @Param("startOfThisWeek") OffsetDateTime startOfThisWeek,
            @Param("startOfLastWeek") OffsetDateTime startOfLastWeek,
            @Param("warehouseId") Long warehouseId);

    @Query("""
        SELECT new com.DTISE.ShelfMasterBE.infrastructure.report.dto.SalesInfoCardResponse(
           SUM(CASE
                   WHEN oi.createdAt >= :startOfThisMonth THEN oi.totalPrice ELSE 0
               END),
           CASE
               WHEN SUM(CASE
                   WHEN oi.createdAt >= :startOfLastMonth
                       AND oi.createdAt < :startOfThisMonth THEN oi.totalPrice ELSE 0
                   END) = 0 THEN NULL
               ELSE (SUM(CASE WHEN oi.createdAt >= :startOfThisMonth
                           THEN oi.totalPrice ELSE 0 END)
                      - SUM(CASE WHEN oi.createdAt >= :startOfLastMonth
                           AND oi.createdAt < :startOfThisMonth THEN oi.totalPrice ELSE 0 END))
                      * 100
                      / SUM(CASE
                           WHEN oi.createdAt >= :startOfLastMonth
                               AND oi.createdAt < :startOfThisMonth THEN oi.totalPrice ELSE 0
                           END)
           END
       )
       FROM ProductMutationOrder pmo
           JOIN ProductMutation pm ON pm.id = pmo.orderedProductMutationId
           JOIN OrderItem oi ON oi.orderId = pmo.orderId
       WHERE pmo.returnedProductMutationId IS NULL
           AND (:warehouseId IS NULL
               OR (pm.mutationType.originType = :originType AND pm.originId = :warehouseId))
           AND pm.mutationType.destinationType = :destinationType
    """)
    SalesInfoCardResponse getTotalSalesThisMonth(
            @Param("originType") MutationEntityType originType,
            @Param("destinationType") MutationEntityType destinationType,
            @Param("startOfThisMonth") OffsetDateTime startOfThisMonth,
            @Param("startOfLastMonth") OffsetDateTime startOfLastMonth,
            @Param("warehouseId") Long warehouseId);

    @Query("""
        SELECT new com.DTISE.ShelfMasterBE.infrastructure.report.dto.SalesInfoCardResponse(
           SUM(CASE
                   WHEN YEAR(oi.createdAt) >= :thisYear THEN oi.totalPrice ELSE 0
               END),
           CASE
               WHEN SUM(CASE
                   WHEN YEAR(oi.createdAt) >= (:thisYear - 1) THEN oi.totalPrice ELSE 0
                   END) = 0 THEN NULL
               ELSE (SUM(CASE WHEN YEAR(oi.createdAt) >= :thisYear THEN oi.totalPrice ELSE 0 END)
                      - SUM(CASE WHEN YEAR(oi.createdAt) >= (:thisYear - 1) THEN oi.totalPrice ELSE 0 END))
                      * 100
                      / SUM(CASE
                               WHEN YEAR(oi.createdAt) >= (:thisYear - 1) THEN oi.totalPrice ELSE 0
                           END)
           END
       )
       FROM ProductMutationOrder pmo
           JOIN ProductMutation pm ON pm.id = pmo.orderedProductMutationId
           JOIN OrderItem oi ON oi.orderId = pmo.orderId
       WHERE pmo.returnedProductMutationId IS NULL
           AND (:warehouseId IS NULL
               OR (pm.mutationType.originType = :originType AND pm.originId = :warehouseId))
           AND pm.mutationType.destinationType = :destinationType
    """)
    SalesInfoCardResponse getTotalSalesThisYear(
            @Param("originType") MutationEntityType originType,
            @Param("destinationType") MutationEntityType destinationType,
            @Param("thisYear") int thisYear,
            @Param("warehouseId") Long warehouseId);

    @Query("""
        SELECT new com.DTISE.ShelfMasterBE.infrastructure.report.dto.PopularProductResponse(
            pm.product.id,
            pm.product.name,
            pm.quantity
       )
       FROM ProductMutationOrder pmo
           JOIN ProductMutation pm ON pm.id = pmo.orderedProductMutationId
       WHERE pmo.returnedProductMutationId IS NULL
           AND pmo.createdAt >= :startOfThisMonth
           AND (:warehouseId IS NULL
               OR (pm.mutationType.originType = :originType AND pm.originId = :warehouseId))
           AND pm.mutationType.destinationType = :destinationType
       ORDER BY pm.quantity DESC
    """)
    List<PopularProductResponse> getThisMonthPopularProducts(
            Pageable pageable,
            @Param("originType") MutationEntityType originType,
            @Param("destinationType") MutationEntityType destinationType,
            @Param("startOfThisMonth") OffsetDateTime startOfThisMonth,
            @Param("warehouseId") Long warehouseId);

    @Query("""
       SELECT new com.DTISE.ShelfMasterBE.infrastructure.report.dto.GraphResponse(
           oi.orderId,
           SUM(COALESCE(oi.totalPrice, 0))
       )
       FROM ProductMutationOrder pmo
           JOIN ProductMutation pm ON pm.id = pmo.orderedProductMutationId
           JOIN OrderItem oi ON oi.orderId = pmo.orderId
       WHERE pmo.returnedProductMutationId IS NULL
           AND (:warehouseId IS NULL
               OR (pm.mutationType.originType = :originType AND pm.originId = :warehouseId))
           AND pm.mutationType.destinationType = :destinationType
           AND oi.createdAt >= :startOfThisMonth
       GROUP BY oi.orderId
    """)
    List<GraphResponse> getSalesGraphThisMonth(
            @Param("originType") MutationEntityType originType,
            @Param("destinationType") MutationEntityType destinationType,
            @Param("startOfThisMonth") OffsetDateTime startOfThisMonth,
            @Param("warehouseId") Long warehouseId);

    @Query("""
       SELECT new com.DTISE.ShelfMasterBE.infrastructure.report.dto.GraphResponse(
           oi.orderId,
           SUM(COALESCE(oi.totalPrice, 0))
       )
       FROM ProductMutationOrder pmo
           JOIN ProductMutation pm ON pm.id = pmo.orderedProductMutationId
           JOIN OrderItem oi ON oi.orderId = pmo.orderId
       WHERE pmo.returnedProductMutationId IS NULL
           AND (:warehouseId IS NULL
               OR (pm.mutationType.originType = :originType AND pm.originId = :warehouseId))
           AND pm.mutationType.destinationType = :destinationType
           AND YEAR(oi.createdAt) >= :thisYear
       GROUP BY oi.orderId
    """)
    List<GraphResponse> getSalesGraphThisYear(
            @Param("originType") MutationEntityType originType,
            @Param("destinationType") MutationEntityType destinationType,
            @Param("thisYear") int thisYear,
            @Param("warehouseId") Long warehouseId);

    @Query("""
       SELECT new com.DTISE.ShelfMasterBE.infrastructure.report.dto.SalesReportResponse(
            oi.orderId,
            p.id,
            p.name,
            CASE
                WHEN :categoryId IS NULL THEN ''
                ELSE c.name
            END,
            oi.quantity,
            oi.totalPrice,
            pmo.createdAt
       )
       FROM ProductMutationOrder pmo
           JOIN ProductMutation pm ON pm.id = pmo.orderedProductMutationId
           JOIN OrderItem oi ON oi.orderId = pmo.orderId
           JOIN pm.product p
           JOIN p.categories c
       WHERE pmo.returnedProductMutationId IS NULL
           AND pmo.createdAt >= :startDate
           AND pmo.createdAt < :endDate
           AND (:productId IS NULL OR p.id = :productId)
           AND (:categoryId IS NULL OR c.id = :categoryId)
           AND (:warehouseId IS NULL
               OR (pm.mutationType.originType = :originType AND pm.originId = :warehouseId))
           AND pm.mutationType.destinationType = :destinationType
       GROUP BY oi.orderId, p.id, p.name, oi.quantity, oi.totalPrice, pmo.createdAt
    """)
    Page<SalesReportResponse> getSalesReportPreview(
            Pageable pageable,
            @Param("originType") MutationEntityType originType,
            @Param("destinationType") MutationEntityType destinationType,
            @Param("productId") Long productId,
            @Param("categoryId") Long categoryId,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate,
            @Param("warehouseId") Long warehouseId);

    @Query("""
       SELECT new com.DTISE.ShelfMasterBE.infrastructure.report.dto.SalesReportResponse(
            oi.orderId,
            p.id,
            p.name,
            CASE
                WHEN :categoryId IS NULL THEN ''
                ELSE c.name
            END,
            oi.quantity,
            oi.totalPrice,
            pmo.createdAt
       )
       FROM ProductMutationOrder pmo
           JOIN ProductMutation pm ON pm.id = pmo.orderedProductMutationId
           JOIN OrderItem oi ON oi.orderId = pmo.orderId
           JOIN pm.product p
           JOIN p.categories c
       WHERE pmo.returnedProductMutationId IS NULL
           AND pmo.createdAt >= :startDate
           AND pmo.createdAt < :endDate
           AND (:productId IS NULL OR p.id = :productId)
           AND (:categoryId IS NULL OR c.id = :categoryId)
           AND (:warehouseId IS NULL
               OR (pm.mutationType.originType = :originType AND pm.originId = :warehouseId))
           AND pm.mutationType.destinationType = :destinationType
       GROUP BY oi.orderId, p.id, p.name, oi.quantity, oi.totalPrice, pmo.createdAt
    """)
    List<SalesReportResponse> getSalesReport(
            @Param("originType") MutationEntityType originType,
            @Param("destinationType") MutationEntityType destinationType,
            @Param("productId") Long productId,
            @Param("categoryId") Long categoryId,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate,
            @Param("warehouseId") Long warehouseId);
}
