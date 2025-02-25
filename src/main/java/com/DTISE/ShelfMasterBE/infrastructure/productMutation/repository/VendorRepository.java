package com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository;

import com.DTISE.ShelfMasterBE.entity.Vendor;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.VendorResponse;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    @Query("SELECT v FROM Vendor v " +
            "WHERE (:search IS NULL OR :search = '' " +
            "     OR LOWER(v.name) LIKE LOWER(CONCAT('%', :search, '%'))) ")
    Page<Vendor> findVendorsBySearch(@Param("search") String search, Pageable pageable);
}
