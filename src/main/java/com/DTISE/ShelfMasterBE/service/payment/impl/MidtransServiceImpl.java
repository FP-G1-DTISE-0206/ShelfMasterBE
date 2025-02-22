package com.DTISE.ShelfMasterBE.service.payment.impl;

import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CartRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentResponse;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.TransactionApi;
import com.midtrans.httpclient.error.MidtransError;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.DTISE.ShelfMasterBE.service.payment.MidtransService;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MidtransServiceImpl implements MidtransService {
    @Override
    public String createTransactionToken(String orderId, Double amount, String paymentMethod, String customerEmail) {
        try {
            Map<String, Object> params = new HashMap<>();
            Map<String, Object> transactionDetails = new HashMap<>();
            transactionDetails.put("order_id", orderId);
            transactionDetails.put("gross_amount", amount);

            Map<String, Object> customerDetails = new HashMap<>();
            customerDetails.put("email", customerEmail);

            params.put("transaction_details", transactionDetails);
            params.put("customer_details", customerDetails);

            if (paymentMethod != null && !paymentMethod.isEmpty()) {
                params.put("enabled_payments", new String[]{paymentMethod});
            }

            return SnapApi.createTransactionToken(params);
        } catch (MidtransError e) {
            throw new RuntimeException("Midtrans Error: " + e.getMessage());
        }
    }

    @Override
    public String checkTransactionStatus(String transactionId) {
        try {
            JSONObject jsonResponse = TransactionApi.getStatusB2b(transactionId);
            Map<String, Object> response = jsonResponse.toMap();

            if (response.containsKey("transaction_status")) {
                return response.get("transaction_status").toString();
            } else {
                throw new RuntimeException("Transaction status not found in response.");
            }
        } catch (MidtransError e) {
            throw new RuntimeException("Failed to check Midtrans status: " + e.getMessage());
        }
    }

    @Override
    public PaymentResponse createTransaction(CartRequest cartRequest) {
        return null;
    }
}
