package com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository;

import com.DTISE.ShelfMasterBE.entity.ProductMutationLogReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMutationLogReasonRepository extends JpaRepository<ProductMutationLogReason, Long> {
}
