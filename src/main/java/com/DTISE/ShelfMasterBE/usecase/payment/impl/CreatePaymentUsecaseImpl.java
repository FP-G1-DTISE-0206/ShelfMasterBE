package com.DTISE.ShelfMasterBE.usecase.payment.impl;

import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.DTISE.ShelfMasterBE.service.payment.MidtransService;
import com.DTISE.ShelfMasterBE.usecase.payment.CreatePaymentUsecase;

@Service
@RequiredArgsConstructor
public class CreatePaymentUsecaseImpl implements CreatePaymentUsecase {

    private final MidtransService midtransService;

    @Override
    public PaymentResponse execute(PaymentRequest request) {

        String transactionToken = midtransService.createTransactionToken(
                request.getOrderId(),
                request.getAmount(),
                request.getPaymentMethod(),
                request.getCustomerEmail()
        );

        return new PaymentResponse(request.getOrderId(), transactionToken, "Pending");
    }
}
