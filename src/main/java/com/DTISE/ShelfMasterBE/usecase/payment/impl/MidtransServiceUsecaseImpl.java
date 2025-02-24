package com.DTISE.ShelfMasterBE.usecase.payment.impl;

import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentResponse;
import com.DTISE.ShelfMasterBE.usecase.payment.MidtransServiceUsecase;
import com.DTISE.ShelfMasterBE.infrastructure.payment.config.MidtransConfig;
import com.midtrans.Config;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MidtransServiceUsecaseImpl implements MidtransServiceUsecase {

    private final MidtransConfig midtransConfig;

    @Override
    public PaymentResponse createTransaction(PaymentRequest request) {
        try {
            String orderId = UUID.randomUUID().toString();

            // Prepare transaction details
            Map<String, Object> transactionDetails = new HashMap<>();
            transactionDetails.put("order_id", orderId);
            transactionDetails.put("gross_amount", request.getTotalAmount());

            // Enable Credit Card transactions
            Map<String, Object> creditCard = new HashMap<>();
            creditCard.put("secure", true);

            // Build request payload
            Map<String, Object> params = new HashMap<>();
            params.put("transaction_details", transactionDetails);
            params.put("credit_card", creditCard);

            // Call Midtrans API to get the token
            String transactionToken = SnapApi.createTransactionToken(params, midtransConfig.getConfig());

            if (transactionToken == null || transactionToken.isEmpty()) {
                throw new RuntimeException("Failed to retrieve transaction token from Midtrans.");
            }

            return new PaymentResponse(orderId, transactionToken, request.getTotalAmount());

        } catch (MidtransError e) {
            throw new RuntimeException("Midtrans Error: " + e.getMessage());
        }
    }
}