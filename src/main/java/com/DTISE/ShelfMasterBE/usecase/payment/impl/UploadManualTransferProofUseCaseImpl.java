package com.DTISE.ShelfMasterBE.usecase.payment.impl;

import com.DTISE.ShelfMasterBE.entity.Payment;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.ManualTransferRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.DTISE.ShelfMasterBE.usecase.payment.UploadManualTransferProofUsecase;

@Service
@RequiredArgsConstructor
public class UploadManualTransferProofUseCaseImpl implements UploadManualTransferProofUsecase {

    private final PaymentRepository paymentRepository;

    @Override
    public void execute(ManualTransferRequest request) {
        Payment payment = paymentRepository.findByOrderId(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!"pending".equals(payment.getTransactionStatus())) {
            throw new RuntimeException("Cannot upload proof for a completed or failed transaction.");
        }

        payment.setManualTransferProof(request.getProofImageUrl());
        paymentRepository.save(payment);
    }
}