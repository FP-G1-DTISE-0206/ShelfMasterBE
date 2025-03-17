package com.DTISE.ShelfMasterBE.usecase.payment.impl;

import com.DTISE.ShelfMasterBE.entity.Payment;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentStatusResponse;
import com.DTISE.ShelfMasterBE.infrastructure.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.DTISE.ShelfMasterBE.usecase.payment.GetPaymentStatusUsecase;

@Service
@RequiredArgsConstructor
public class GetPaymentStatusUseCaseImpl implements GetPaymentStatusUsecase {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentStatusResponse execute(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return new PaymentStatusResponse(
                payment.getOrder().getId(),
                payment.getPaymentMethod().getName(),
                payment.getTransactionId(),
                payment.getTransactionStatus(),
                payment.getManualTransferProof(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}
