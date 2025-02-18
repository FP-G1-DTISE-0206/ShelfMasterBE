package com.DTISE.ShelfMasterBE.usecase.payment.impl;

import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CartItemRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentResponse;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentStatusResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.payment.MidtransServiceUsecase;
import com.DTISE.ShelfMasterBE.entity.Product;
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MidtransServiceUsecaseImpl implements MidtransServiceUsecase {

    private static final Logger logger = LoggerFactory.getLogger(MidtransServiceUsecaseImpl.class);

    @Qualifier("midtransConfigBean")
    private final Config midtransConfig;
    private final ProductRepository productRepository;

    @Override
    public PaymentResponse createTransaction(PaymentRequest request){
        try{
            Map<String, Object> params = new HashMap<>();
            Map<String, Object> transactionDetails = new HashMap<>();

            transactionDetails.put("order_id", request.getOrderId());
            transactionDetails.put("gross_amount", request.getAmount());

            params.put("transaction_details", transactionDetails);

            // Ensure cartItems is not null or empty before proceeding
            if (request.getCartItems() == null || request.getCartItems().isEmpty()) {
                throw new IllegalArgumentException("Cart items cannot be null or empty");
            }


            // Calculate total amount
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (CartItemRequest itemRequest : request.getCartItems()) {
                Product product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemRequest.getProductId()));
                totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
            }


            logger.info("Requesting Midtrans Transaction for Order ID: {}", request.getOrderId());

            JSONObject response = SnapApi.createTransaction(params, midtransConfig);

//            String transactionToken = response.getString("token");
//            String paymentUrl = response.getString("redirect_url");

            String transactionToken = response.optString("token", null);
//            String paymentUrl = response.optString("redirect_url", null);
            String paymentUrl = "https://app.sandbox.midtrans.com/snap/v2/v2.js?token=" + transactionToken;


            if(transactionToken == null) {
                logger.error("Missing expected fields in Midtrans response for order ID: {}", request.getOrderId());
                return new PaymentResponse(null,null,"FAILED",null);
            }

            logger.info("Midtrans Response - Transaction ID: {}, Payment URL: {}", request.getOrderId(), paymentUrl);

            //            paymentResponse.setTotalAmount(totalAmount);
//            return new PaymentResponse(request.getOrderId(), paymentUrl, "Pending");
            return new PaymentResponse(request.getOrderId(), paymentUrl, "PENDING", totalAmount);
        } catch(MidtransError e){
            logger.error("Midtrans Error: {}", e.getMessage(), e);
            return new PaymentResponse(null,null,"FAILED",null);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid request: {}", e.getMessage());
            return new PaymentResponse(null, null, "FAILED", null);
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