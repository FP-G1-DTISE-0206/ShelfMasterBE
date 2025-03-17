package com.DTISE.ShelfMasterBE.usecase.payment.impl;

import com.DTISE.ShelfMasterBE.entity.Payment;
import com.DTISE.ShelfMasterBE.infrastructure.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.DTISE.ShelfMasterBE.usecase.payment.VerifyManualTransferUsecase;

@Service
@RequiredArgsConstructor
public class VerifyManualTransferUseCaseImpl implements VerifyManualTransferUsecase {

    private final PaymentRepository paymentRepository;

    @Override
    public void execute(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setTransactionStatus("confirmed");
        paymentRepository.save(payment);
    }
}
