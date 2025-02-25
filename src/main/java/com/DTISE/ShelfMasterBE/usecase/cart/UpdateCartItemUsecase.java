package com.DTISE.ShelfMasterBE.usecase.cart;

import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CreateCartItemResponse;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.UpdateCartItemRequest;

public interface UpdateCartItemUsecase {
    CreateCartItemResponse execute(UpdateCartItemRequest request);
}
