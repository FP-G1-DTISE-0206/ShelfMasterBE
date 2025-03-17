package com.DTISE.ShelfMasterBE.infrastructure.order.controller;

import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import com.DTISE.ShelfMasterBE.common.tools.Pagination;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.order.dto.CreateOrderRequest;
import com.DTISE.ShelfMasterBE.infrastructure.order.dto.GetOrderResponse;
import com.DTISE.ShelfMasterBE.usecase.order.CreateOrderUsecase;
import com.DTISE.ShelfMasterBE.usecase.order.GetOrderUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUsecase createOrderUsecase;
    private final GetOrderUsecase getOrderUsecase;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        String email = Claims.getEmailFromJwt();

        return ApiResponse.successfulResponse("Order created successfully", createOrderUsecase.execute(request, email));
    }

    @GetMapping
    public ResponseEntity<?> getOrder(
            @RequestParam int start,
            @RequestParam int length,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String field,
            @RequestParam(required = false) String order
    ) {
        String email = Claims.getEmailFromJwt();
        return ApiResponse.successfulResponse(
                "Order retrieved successfully",
                Pagination.mapResponse(getOrderUsecase.getOrders(
                        Pagination.createPageable(start, length, field, order),
                        search, email))
        );
    }
}


