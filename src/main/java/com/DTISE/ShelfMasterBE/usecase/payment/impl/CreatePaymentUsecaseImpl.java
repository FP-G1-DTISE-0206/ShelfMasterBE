package com.DTISE.ShelfMasterBE.usecase.payment.impl;

import com.DTISE.ShelfMasterBE.entity.Order;
import com.DTISE.ShelfMasterBE.entity.Payment;
import com.DTISE.ShelfMasterBE.infrastructure.order.dto.CreateOrderRequest;
import com.DTISE.ShelfMasterBE.infrastructure.order.repository.OrderRepository;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.*;
import com.DTISE.ShelfMasterBE.infrastructure.payment.repository.PaymentRepository;
import com.DTISE.ShelfMasterBE.usecase.payment.CreatePaymentUsecase;
import com.DTISE.ShelfMasterBE.usecase.payment.MidtransPaymentUseCase;
import com.DTISE.ShelfMasterBE.usecase.payment.MidtransServiceUsecase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.DTISE.ShelfMasterBE.usecase.payment.CreatePaymentUsecase;
import com.DTISE.ShelfMasterBE.usecase.payment.MidtransPaymentUseCase;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CreatePaymentUsecaseImpl implements CreatePaymentUsecase {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final MidtransPaymentUseCase midtransPaymentUseCase;



    @Override
    @Transactional
    public CreatePaymentResponse execute(CreatePaymentRequest request, String email) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(order.getPaymentMethod());
        payment.setAmount(order.getFinalPrice().doubleValue());
        payment.setCreatedAt(OffsetDateTime.now());
        payment.setUpdatedAt(OffsetDateTime.now());

        if (request.getPaymentMethodId() == 1) {
            MidtransTransactionRequest midtransRequest = new MidtransTransactionRequest(
                    "ORDER-" + order.getId(),
                    order.getFinalPrice().doubleValue(),
                    "bank_transfer"
            );
            MidtransTransactionResponse midtransResponse = midtransPaymentUseCase.createTransaction(midtransRequest);

            payment.setTransactionId(midtransResponse.getTransactionId());
            payment.setTransactionStatus("pending");
            payment.setMidtransRedirectUrl(midtransResponse.getRedirectUrl());
            payment.setManualTransferProof(null);
        } else if (request.getPaymentMethodId() == 2) {
            payment.setTransactionStatus("pending");
            payment.setManualTransferProof(request.getManualTransferProof());
            payment.setMidtransRedirectUrl(null);
        } else {
            throw new RuntimeException("Unsupported payment method.");
        }

        paymentRepository.save(payment);

        return new CreatePaymentResponse(
                order.getId(),
                order.getPaymentMethod().getName(),
                payment.getTransactionStatus(),
                payment.getMidtransRedirectUrl(),
                payment.getManualTransferProof()
        );
    }
}

