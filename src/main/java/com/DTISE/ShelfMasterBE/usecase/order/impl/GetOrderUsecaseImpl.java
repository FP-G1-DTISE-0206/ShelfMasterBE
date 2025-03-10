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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetOrderUsecaseImpl implements GetOrderUsecase {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional(readOnly = true)
    public List<GetOrderResponse> execute(String email) {
        User user = userRepository.findByEmailContainsIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<GetOrderResponse> orders = orderRepository.findByUser(user).stream()
                .filter(order -> order.getDeletedAt() == null)
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .map(order -> new GetOrderResponse(
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
                ))
                .collect(Collectors.toList());

        if (orders.isEmpty()) {
            throw new RuntimeException("No orders found for user " + email);
        }

        return orders;
    }


}
