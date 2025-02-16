package com.DTISE.ShelfMasterBE.usecase.productMutation;

import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.ProductMutationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetProductMutationUseCase {
    Page<ProductMutationResponse> getProductMutations(Pageable pageable, Long WarehouseId);
}
