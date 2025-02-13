package com.DTISE.ShelfMasterBE.usecase.payment.impl;

import com.midtrans.httpclient.TransactionApi;
import com.midtrans.httpclient.error.MidtransError;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentStatusResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.DTISE.ShelfMasterBE.usecase.payment.CheckPaymentStatusUsecase;

@Service
@RequiredArgsConstructor
public class CheckPaymentStatusUsecaseImpl implements CheckPaymentStatusUsecase {

    @Override
    public PaymentStatusResponse execute(String transactionId) {
        try {
            JSONObject response = TransactionApi.getStatusB2b(transactionId);

            return new PaymentStatusResponse(
                    response.getString("order_id"),
                    response.getString("transaction_status"),
                    response.getString("gross_amount"),
                    response.getString("transaction_time")
            );
        } catch (MidtransError e) {
            throw new RuntimeException("Failed to check payment status: " + e.getMessage());
        }
    }
}
