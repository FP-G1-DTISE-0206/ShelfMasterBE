package com.DTISE.ShelfMasterBE.infrastructure.category.repository;

import com.DTISE.ShelfMasterBE.entity.Category;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Boolean existsByName(String name);
    Optional<Category> getFirstByName(String name);

    @Query("""
        SELECT c FROM Category c
        WHERE (:search IS NULL OR :search = ''
        OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')))
    """)
    Page<Category> findAllBySearch(@Param("search") String search, Pageable pageable);
}
