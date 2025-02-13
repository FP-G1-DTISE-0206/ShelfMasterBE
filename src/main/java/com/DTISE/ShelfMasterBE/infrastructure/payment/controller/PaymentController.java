package com.DTISE.ShelfMasterBE.infrastructure.payment.controller;

import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentResponse;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentStatusResponse;
import com.DTISE.ShelfMasterBE.usecase.payment.CheckPaymentStatusUsecase;
import com.DTISE.ShelfMasterBE.usecase.payment.CreatePaymentUsecase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final CreatePaymentUsecase createPaymentUsecase;
    private final CheckPaymentStatusUsecase checkPaymentStatusUsecase;


    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createTransaction(@Valid @RequestBody PaymentRequest request) {

        logger.info("Received Payment Request: {}", request);

        PaymentResponse response = createPaymentUsecase.execute(request);
        if(response.getTransactionId() == null || response.getPaymentUrl() == null) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);

    }

    @GetMapping("/status/{transactionId}")
    public ResponseEntity<PaymentStatusResponse> checkPaymentStatus(@PathVariable String transactionId) {
        logger.info("Checking Payment Status for Transaction ID: {}", transactionId);

        PaymentStatusResponse response = checkPaymentStatusUsecase.execute(transactionId);
        return ResponseEntity.ok(response);

    }
}
