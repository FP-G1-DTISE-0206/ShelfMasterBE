package com.DTISE.ShelfMasterBE.infrastructure.promotion.repository;

import com.DTISE.ShelfMasterBE.entity.Promotion;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Optional<Promotion> findById(Long id);

    @Query("SELECT p FROM Promotion p " +
            "WHERE (:search IS NULL OR :search = '' " +
            "     OR LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "     OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))) ")
    Page<Promotion> findPromotionBySearch(@Param("search") String search, Pageable pageable);
}
