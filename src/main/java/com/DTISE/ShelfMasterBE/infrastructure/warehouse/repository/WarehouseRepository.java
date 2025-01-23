package com.DTISE.ShelfMasterBE.infrastructure.warehouse.repository;

import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.entity.Warehouse;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findById(Long id);

    @Query("SELECT w FROM Warehouse w " +
            "WHERE (:search IS NULL OR :search = '' " +
            "     OR LOWER(w.name) LIKE LOWER(CONCAT('%', :search, '%'))) ")
    Page<Warehouse> findWarehousesBySearch(@Param("search") String search, Pageable pageable);
}
