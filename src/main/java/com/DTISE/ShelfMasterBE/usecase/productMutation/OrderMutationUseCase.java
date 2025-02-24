package com.DTISE.ShelfMasterBE.usecase.productMutation;

import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.AutoMutationRequest;

public interface OrderMutationUseCase {
    Long orderMutateAll(Long userId, Long orderId, Long warehouseId);

    Long getProductTotalStock(Long productId);
}
