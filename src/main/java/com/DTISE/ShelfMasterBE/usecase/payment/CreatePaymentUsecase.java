package com.DTISE.ShelfMasterBE.usecase.payment;

import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentResponse;

public interface CreatePaymentUsecase {
    PaymentResponse execute(PaymentRequest request);
}
