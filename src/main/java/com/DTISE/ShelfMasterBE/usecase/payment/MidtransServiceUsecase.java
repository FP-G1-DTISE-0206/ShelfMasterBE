package com.DTISE.ShelfMasterBE.usecase.payment;

import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentResponse;
//import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentStatusResponse;

public interface MidtransServiceUsecase {
    PaymentResponse createTransaction(PaymentRequest request);
//    PaymentStatusResponse checkTransactionStatus(String transactionId);
}
