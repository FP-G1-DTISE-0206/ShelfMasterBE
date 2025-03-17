package com.DTISE.ShelfMasterBE.usecase.order;

import com.DTISE.ShelfMasterBE.infrastructure.order.dto.CreateOrderRequest;
import com.DTISE.ShelfMasterBE.infrastructure.order.dto.CreateOrderResponse;

public interface CreateOrderUsecase {
    CreateOrderResponse execute (CreateOrderRequest request, String email);
}
