package com.DTISE.ShelfMasterBE.usecase.order.impl;

import com.DTISE.ShelfMasterBE.entity.Order;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.order.dto.GetOrderResponse;
import com.DTISE.ShelfMasterBE.infrastructure.order.dto.OrderItemResponse;
import com.DTISE.ShelfMasterBE.infrastructure.order.repository.OrderRepository;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseResponse;
import com.DTISE.ShelfMasterBE.usecase.order.GetOrderUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetOrderUsecaseImpl implements GetOrderUsecase {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional(readOnly = true)
    public Page<GetOrderResponse> getOrders(Pageable pageable, String search, String email) {
        User user = userRepository.findByEmailContainsIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("SUPER_ADMIN"))) {
            return orderRepository.findBySearch(search, pageable).map(order -> new GetOrderResponse(
                    order.getId(),
                    new UserResponse(order.getUser()),
                    order.getLatestStatus().getName(),
                    order.getPaymentMethod().getName(),
                    new WarehouseResponse(order.getWarehouse()),
                    order.getMidtransTokenUrl(),
                    order.getManualTransferProof(),
                    order.getTotalPrice(),
                    order.getIsPaid(),
                    order.getAddressId(),
                    order.getOrderItems().stream().map(OrderItemResponse::new).collect(Collectors.toList())
            ));
        }

        if (user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("WH_ADMIN"))) {
            return orderRepository.findByAdminAndSearch(user, search, pageable).map(order -> new GetOrderResponse(
                    order.getId(),
                    new UserResponse(order.getUser()),
                    order.getLatestStatus().getName(),
                    order.getPaymentMethod().getName(),
                    new WarehouseResponse(order.getWarehouse()),
                    order.getMidtransTokenUrl(),
                    order.getManualTransferProof(),
                    order.getTotalPrice(),
                    order.getIsPaid(),
                    order.getAddressId(),
                    order.getOrderItems().stream().map(OrderItemResponse::new).collect(Collectors.toList())
            ));
        }

        return orderRepository.findByUserAndSearch(user, search, pageable).map(order -> new GetOrderResponse(
                order.getId(),
                new UserResponse(order.getUser()),
                order.getLatestStatus().getName(),
                order.getPaymentMethod().getName(),
                new WarehouseResponse(order.getWarehouse()),
                order.getMidtransTokenUrl(),
                order.getManualTransferProof(),
                order.getTotalPrice(),
                order.getIsPaid(),
                order.getAddressId(),
                order.getOrderItems().stream().map(OrderItemResponse::new).collect(Collectors.toList())
        ));
    }
}
