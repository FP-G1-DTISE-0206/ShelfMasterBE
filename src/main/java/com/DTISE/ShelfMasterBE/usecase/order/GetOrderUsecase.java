package com.DTISE.ShelfMasterBE.usecase.order;

import com.DTISE.ShelfMasterBE.infrastructure.order.dto.GetOrderResponse;

import java.util.List;

public interface GetOrderUsecase {
    List<GetOrderResponse> execute(String email);
//    List<GetOrderResponse> getOrdersByUserId(Long userId);
}
