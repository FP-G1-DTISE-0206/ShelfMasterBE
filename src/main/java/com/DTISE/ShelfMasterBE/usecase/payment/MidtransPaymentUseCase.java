package com.DTISE.ShelfMasterBE.usecase.payment;

import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.MidtransTransactionRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.MidtransTransactionResponse;

public interface MidtransPaymentUseCase {
    MidtransTransactionResponse createTransaction(MidtransTransactionRequest request);
    MidtransTransactionResponse getTransactionStatus(String orderId);
}
