package com.DTISE.ShelfMasterBE.infrastructure.product.repository;

import com.DTISE.ShelfMasterBE.entity.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Boolean existsByName(String name);
    Optional<Product> getFirstByName(String name);

    @Query("""
        SELECT p FROM Product p LEFT JOIN p.categories c
        WHERE (:search IS NULL OR :search = ''
        OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')))
    """)
    Page<Product> findAllBySearch(@Param("search") String search, Pageable pageable);
}
