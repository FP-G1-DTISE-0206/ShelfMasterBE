package com.DTISE.ShelfMasterBE.service.payment;

import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentResponse;

public interface MidtransService {
    PaymentResponse createTransaction(PaymentRequest request);
}
