package com.DTISE.ShelfMasterBE.usecase.cart;

import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CreateCartItemRequest;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CreateCartItemResponse;

public interface AddToCartUsecase {
    CreateCartItemResponse execute(CreateCartItemRequest request);
}
