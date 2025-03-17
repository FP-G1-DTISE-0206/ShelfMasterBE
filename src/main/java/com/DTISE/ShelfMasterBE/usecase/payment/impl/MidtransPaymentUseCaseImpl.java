package com.DTISE.ShelfMasterBE.usecase.payment.impl;

import com.DTISE.ShelfMasterBE.infrastructure.payment.config.MidtransConfig;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.MidtransTransactionRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.MidtransTransactionResponse;
import com.DTISE.ShelfMasterBE.usecase.payment.MidtransPaymentUseCase;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.TransactionApi;
import com.midtrans.httpclient.error.MidtransError;
import jakarta.transaction.Transaction;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MidtransPaymentUseCaseImpl implements MidtransPaymentUseCase {

    @Override
    public MidtransTransactionResponse createTransaction(MidtransTransactionRequest request) {
        try {
            // Set Midtrans Global Config
            Midtrans.serverKey = "SB-Mid-server-n4ucrpa9FgTPd6EpJer3mjR3";
            Midtrans.isProduction = false; // Change to true for production

            // Generate a unique order ID
            UUID uniqueOrderId = UUID.randomUUID();

            // Prepare transaction details
            Map<String, Object> transactionParams = new HashMap<>();
            Map<String, String> transactionDetails = new HashMap<>();
            transactionDetails.put("order_id", uniqueOrderId.toString());
            transactionDetails.put("gross_amount", String.valueOf(request.getAmount()));

            Map<String, String> creditCard = new HashMap<>();
            creditCard.put("secure", "true");

            transactionParams.put("transaction_details", transactionDetails);
            transactionParams.put("credit_card", creditCard);


            String transactionToken = SnapApi.createTransactionToken(transactionParams);

            return new MidtransTransactionResponse(
                    uniqueOrderId.toString(),
                    "pending",
                    "https://app.sandbox.midtrans.com/snap/v2/vtweb/" + transactionToken
            );
        } catch (MidtransError e) {
            throw new RuntimeException("Midtrans transaction creation failed: " + e.getMessage());
        }
    }

    @Override
    public MidtransTransactionResponse getTransactionStatus(String orderId) {
        try {

            JSONObject jsonResponse = TransactionApi.getStatusB2b(orderId);

            String transactionId = jsonResponse.optString("transaction_id", "N/A");
            String transactionStatus = jsonResponse.optString("transaction_status", "unknown");

            return new MidtransTransactionResponse(
                    transactionId,
                    transactionStatus,
                    null
            );
        } catch (MidtransError e) {
            throw new RuntimeException("Failed to get transaction status: " + e.getMessage());
        }
    }
}