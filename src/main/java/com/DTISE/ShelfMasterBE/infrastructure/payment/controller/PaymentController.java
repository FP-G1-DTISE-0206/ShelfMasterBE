package com.DTISE.ShelfMasterBE.infrastructure.payment.controller;

import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.*;
import com.DTISE.ShelfMasterBE.usecase.payment.CreatePaymentUsecase;
import com.DTISE.ShelfMasterBE.usecase.payment.GetPaymentStatusUsecase;
import com.DTISE.ShelfMasterBE.usecase.payment.HandleMidtransWebhookUsecase;
import com.DTISE.ShelfMasterBE.usecase.payment.MidtransServiceUsecase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final CreatePaymentUsecase createPaymentUsecase;
    private final GetPaymentStatusUsecase getPaymentStatusUsecase;
    private final HandleMidtransWebhookUsecase handleMidtransWebhookUsecase;

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody CreatePaymentRequest request) {
        String email = Claims.getEmailFromJwt();
        CreatePaymentResponse response = createPaymentUsecase.execute(request, email);
        return ApiResponse.successfulResponse("Payment created successfully", response);
    }

    @GetMapping("/status/{orderId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable Long orderId) {
        PaymentStatusResponse response = getPaymentStatusUsecase.execute(orderId);
        return ApiResponse.successfulResponse("Payment status retrieved successfully", response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> handleMidtransWebhook(@RequestBody Map<String, Object> payload) {
        handleMidtransWebhookUsecase.execute(payload);
        return ResponseEntity.ok("Webhook processed successfully");
    }
}

//private final CreatePaymentUsecase createPaymentUsecase;
//private final CheckPaymentStatusUsecase checkPaymentStatusUsecase;
//
//@PostMapping("/create")
//public ResponseEntity<PaymentResponse> createTransaction(@Valid @RequestBody PaymentRequest request) {
//
//    log.info("Received Payment Request: {}", request);
//
//    PaymentResponse response = createPaymentUsecase.execute(request);
//    return ResponseEntity.ok(response);
//}
//
//
//@GetMapping("/status/{transactionId}")
//public ResponseEntity<?> checkPaymentStatus(@PathVariable String transactionId) {
//    PaymentStatusResponse response = checkPaymentStatusUsecase.execute(transactionId);
//    return ApiResponse.successfulResponse("Payment status retrieved successfully", response);
//}

