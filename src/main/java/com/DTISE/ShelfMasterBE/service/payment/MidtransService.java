package com.DTISE.ShelfMasterBE.service.payment;

import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CartRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentResponse;

public interface MidtransService {
    String createTransactionToken(String orderId, Double amount, String paymentMethod, String customerEmail);
    String checkTransactionStatus(String transactionId);

    PaymentResponse createTransaction(CartRequest cartRequest);
}
