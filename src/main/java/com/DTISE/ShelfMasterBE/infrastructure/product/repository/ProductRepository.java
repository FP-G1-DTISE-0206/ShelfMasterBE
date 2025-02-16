package com.DTISE.ShelfMasterBE.infrastructure.product.repository;

import com.DTISE.ShelfMasterBE.entity.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Boolean existsByName(String name);
    Optional<Product> getFirstByName(String name);

    @Query("""
        SELECT p FROM Product p
        WHERE (:search IS NULL OR :search = ''
        OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:categoryIds IS NULL OR (
            EXISTS (SELECT 1 FROM p.categories c WHERE c.id IN :categoryIds)
            AND SIZE(p.categories) > 0
        ))
    """)
    Page<Product> findAllBySearchAndCategoryIds(
            @Param("search") String search,
            @Param("categoryIds") List<Long> categoryIds,
            Pageable pageable
    );

    @Query("""
        SELECT p FROM Product p
        WHERE (:search IS NULL OR :search = ''
        OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (EXISTS (SELECT 1 FROM p.stock s WHERE s.warehouse_id = :warehouseId))
    """)
    Page<Product> findAllBySearchAndWarehouseId(
            @Param("search") String search,
            Pageable pageable,
            @Param("warehouseId") Long warehouseId
    );
}
