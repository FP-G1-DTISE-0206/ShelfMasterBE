package com.DTISE.ShelfMasterBE.usecase.payment.impl;

import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentResponse;
import com.DTISE.ShelfMasterBE.service.payment.MidtransService;
import com.DTISE.ShelfMasterBE.usecase.payment.CreatePaymentUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePaymentUsecaseImpl implements CreatePaymentUsecase {

    private final MidtransService midtransService;

    @Override
    public PaymentResponse execute(PaymentRequest request) {
        return midtransService.createTransaction(request);
    }
}
