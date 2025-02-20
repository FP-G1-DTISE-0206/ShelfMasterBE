package com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository;

import com.DTISE.ShelfMasterBE.entity.ProductMutationLog;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.ProductMutationLogResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductMutationLogRepository extends JpaRepository<ProductMutationLog, Long> {
    @Query("""
        SELECT new com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.ProductMutationLogResponse(
           pml.id,
           pml.mutationStatus.name,
           pml.createdAt
       )
       FROM ProductMutationLog pml
       WHERE pml.productMutationId = :productMutationId
    """)
    List<ProductMutationLogResponse> findAllByProductMutationId(@Param("productMutationId") Long productMutationId);
}
