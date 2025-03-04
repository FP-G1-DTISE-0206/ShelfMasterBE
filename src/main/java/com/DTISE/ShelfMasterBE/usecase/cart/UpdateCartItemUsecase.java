package com.DTISE.ShelfMasterBE.usecase.cart;

import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.UpdateCartItemRequest;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.UpdateCartItemResponse;

public interface UpdateCartItemUsecase {
    UpdateCartItemResponse execute(Long cartId, UpdateCartItemRequest request);
}
