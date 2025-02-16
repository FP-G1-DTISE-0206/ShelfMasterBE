package com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository;

import com.DTISE.ShelfMasterBE.entity.ProductMutationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMutationLogRepository extends JpaRepository<ProductMutationLog, Long> {
}
