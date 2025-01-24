package com.DTISE.ShelfMasterBE.infrastructure.category.repository;

import com.DTISE.ShelfMasterBE.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Boolean existsByName(String name);
    Optional<Category> findFirstIdByName(String name);
}
