package com.DTISE.ShelfMasterBE.service.payment.impl;

import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentResponse;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentStatusResponse;
import com.DTISE.ShelfMasterBE.service.payment.MidtransService;
import com.midtrans.Config;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.TransactionApi;
import com.midtrans.httpclient.error.MidtransError;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MidtransServiceImpl implements MidtransService {

    private static final Logger logger = LoggerFactory.getLogger(MidtransServiceImpl.class);

    @Qualifier("midtransConfigBean")
    private final Config midtransConfig;

    @Override
    public PaymentResponse createTransaction(PaymentRequest request){
        try{
            Map<String, Object> params = new HashMap<>();
            Map<String, Object> transactionDetails = new HashMap<>();

            transactionDetails.put("order_id", request.getOrderId());
            transactionDetails.put("gross_amount", request.getAmount());

            params.put("transaction_details", transactionDetails);

            logger.info("Requesting Midtrans Transaction for Order ID: {}", request.getOrderId());

            JSONObject response = SnapApi.createTransaction(params, midtransConfig);

            String transactionToken = response.getString("token");
            String paymentUrl = response.getString("redirect_url");

            logger.info("Midtrans Response - Transaction ID: {}, Payment URL: {}", request.getOrderId(), paymentUrl);

            return new PaymentResponse(request.getOrderId(), paymentUrl, "Pending");

        } catch(MidtransError e){
            logger.error("Midtrans Error: {}", e.getMessage(), e);
            return new PaymentResponse(null,null,"FAILED");
        }
    }


    @Override
    public PaymentStatusResponse checkTransactionStatus(String transactionId){
        try{
            JSONObject response = TransactionApi.checkTransaction(transactionId, midtransConfig);

            logger.info("Midtrans API Response: {}", response.toString());

            String status = response.has("transaction_status") ? response.getString("transaction_status") : "UNKNOWN";
            logger.info("Midtrans Status Check - Transaction ID: {}, Status: {}", transactionId, status);

            return new PaymentStatusResponse(transactionId, status);

        } catch(MidtransError e){
            logger.error("Failed to check Midtrans status: {}", e.getMessage(), e);
            return new PaymentStatusResponse(transactionId, "FAILED");
        }
    }
}