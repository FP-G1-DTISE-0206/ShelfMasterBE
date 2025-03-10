package com.DTISE.ShelfMasterBE.usecase.order.impl;

import com.DTISE.ShelfMasterBE.entity.*;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.GetCartResponse;
import com.DTISE.ShelfMasterBE.infrastructure.order.dto.CreateOrderRequest;
import com.DTISE.ShelfMasterBE.infrastructure.order.dto.CreateOrderResponse;
import com.DTISE.ShelfMasterBE.infrastructure.order.repository.OrderRepository;
import com.DTISE.ShelfMasterBE.infrastructure.order.repository.OrderStatusRepository;
import com.DTISE.ShelfMasterBE.infrastructure.payment.PaymentMethodRepository;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.repository.WarehouseRepository;
import com.DTISE.ShelfMasterBE.usecase.cart.impl.GetCartUsecaseImpl;
import com.DTISE.ShelfMasterBE.usecase.order.CreateOrderUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CreateOrderUsecaseImpl implements CreateOrderUsecase {

    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final UserRepository userRepository;
    private final GetCartUsecaseImpl getCartUsecase;
    private final PaymentMethodRepository paymentMethodRepository;
    private final WarehouseRepository warehouseRepository;


    @Override
    @Transactional
    public CreateOrderResponse execute(CreateOrderRequest request, String email) {
        User user = userRepository.findByEmailContainsIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        GetCartResponse cartResponse = getCartUsecase.execute(email);
        OrderStatus defaultStatus = orderStatusRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Default order status not found"));

        PaymentMethod paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Invalid payment method"));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId()).orElseThrow(() -> new RuntimeException("Invalid warehouse"));


        Order order = new Order();
        order.setUser(user);
        order.setAddressId(request.getAddressId());
        order.setTotalPrice(cartResponse.getTotalPrice());
        order.setLatestStatus(defaultStatus);
        order.setPaymentMethod(paymentMethod);
        order.setWarehouse(warehouse);
        order.setIsPaid(false);
        order.setCreatedAt(OffsetDateTime.now());
        order.setUpdatedAt(OffsetDateTime.now());

        if (request.getPaymentMethodId() == 1) {
            order.setMidtransTokenUrl("MidtransToken123");
            order.setManualTransferProof(null);
        } else if (request.getPaymentMethodId() == 2) {
            order.setManualTransferProof("https://dummy-qrcode.com/payment-qr-123");
            order.setMidtransTokenUrl(null);
        } else {
            throw new RuntimeException("Unsupported payment method.");
        }

        Order savedOrder = orderRepository.save(order);

        // Return response
        return new CreateOrderResponse(
                savedOrder.getId(),
                savedOrder.getLatestStatus().getName(),
                savedOrder.getPaymentMethod().getName(),
                savedOrder.getIsPaid(),
                savedOrder.getTotalPrice(),
                savedOrder.getAddressId()
        );
    }
}
