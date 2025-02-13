package com.DTISE.ShelfMasterBE.usecase.payment.impl;

import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentStatusResponse;
import com.DTISE.ShelfMasterBE.service.payment.MidtransService;
import com.DTISE.ShelfMasterBE.usecase.payment.CheckPaymentStatusUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckPaymentStatusUsecaseImpl implements CheckPaymentStatusUsecase {

    private final MidtransService midtransService;

    @Override
    public PaymentStatusResponse execute(String transactionId) {
        return midtransService.checkTransactionStatus(transactionId);
    }
}
