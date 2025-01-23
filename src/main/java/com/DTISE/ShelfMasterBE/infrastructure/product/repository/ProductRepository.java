package com.DTISE.ShelfMasterBE.infrastructure.product.repository;

import com.DTISE.ShelfMasterBE.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Boolean existsByName(String name);
}
