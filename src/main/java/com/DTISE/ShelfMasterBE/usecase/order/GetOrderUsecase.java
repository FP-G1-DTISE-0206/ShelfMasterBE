package com.DTISE.ShelfMasterBE.usecase.order;

import com.DTISE.ShelfMasterBE.infrastructure.order.dto.GetOrderResponse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GetOrderUsecase {
//    List<GetOrderResponse> execute(String email);
    Page<GetOrderResponse> getOrders(Pageable pageable, String search, String email);
//    List<GetOrderResponse> getOrdersByUserId(Long userId);
}
