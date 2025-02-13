package com.DTISE.ShelfMasterBE.service.payment;

public interface MidtransService {
    String createTransactionToken(String orderId, Double amount, String paymentMethod, String customerEmail);
    String checkTransactionStatus(String transactionId);
}
