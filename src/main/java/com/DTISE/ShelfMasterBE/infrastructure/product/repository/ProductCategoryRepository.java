package com.DTISE.ShelfMasterBE.infrastructure.product.repository;

import com.DTISE.ShelfMasterBE.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    Optional<ProductCategory> findByCategoryId(Long categoryId);
}
