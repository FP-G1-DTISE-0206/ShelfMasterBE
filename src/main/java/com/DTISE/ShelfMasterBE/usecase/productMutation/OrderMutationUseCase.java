package com.DTISE.ShelfMasterBE.usecase.productMutation;

import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.AutoMutationRequest;

public interface OrderMutationUseCase {
    Long orderMutateAll(AutoMutationRequest request);

    Long getProductTotalStock(Long productId);
}
