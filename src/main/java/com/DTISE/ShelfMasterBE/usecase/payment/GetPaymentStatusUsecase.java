package com.DTISE.ShelfMasterBE.usecase.payment;

import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentStatusResponse;

public interface GetPaymentStatusUsecase {
    PaymentStatusResponse execute(Long orderId);
}
