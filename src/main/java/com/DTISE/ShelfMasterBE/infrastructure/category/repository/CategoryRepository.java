package com.DTISE.ShelfMasterBE.infrastructure.category.repository;

import com.DTISE.ShelfMasterBE.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Boolean existsByName(String name);
    Long findFirstIdByName(String name);
}
