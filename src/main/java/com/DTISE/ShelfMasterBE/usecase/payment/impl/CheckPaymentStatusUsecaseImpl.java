package com.DTISE.ShelfMasterBE.usecase.payment.impl;

import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckPaymentStatusUsecaseImpl implements CheckPaymentStatusUsecase {

    @Override
    public PaymentStatusResponse execute(String transactionId) {
        return midtransService.checkTransactionStatus(transactionId);
    }
}
