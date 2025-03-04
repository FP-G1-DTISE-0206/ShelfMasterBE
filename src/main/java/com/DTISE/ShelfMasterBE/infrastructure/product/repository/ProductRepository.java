package com.DTISE.ShelfMasterBE.infrastructure.product.repository;

import com.DTISE.ShelfMasterBE.entity.Product;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Boolean existsBySku(String sku);
    Boolean existsByName(String name);
    Optional<Product> getFirstBySku(String sku);
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
    Page<Product> findAllBySearchAndOrWarehouseId(
            @Param("search") String search,
            @Param("categoryIds") List<Long> categoryIds,
            Pageable pageable
    );

    Optional<Object> findById(@NotNull BigInteger productId);
}
