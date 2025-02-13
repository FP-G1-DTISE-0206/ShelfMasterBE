package com.DTISE.ShelfMasterBE.infrastructure.payment.controller;

import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentResponse;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final CreatePaymentUsecase createPaymentUsecase;
    private final CheckPaymentStatusUsecase checkPaymentStatusUsecase;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createTransaction(@Valid @RequestBody PaymentRequest request) {

        logger.info("Received Payment Request: {}", request);

        PaymentResponse response = createPaymentUsecase.execute(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{transactionId}")
    public ResponseEntity<PaymentStatusResponse> checkPaymentStatus(@PathVariable String transactionId) {
        PaymentStatusResponse response = checkPaymentStatusUsecase.execute(transactionId);
        return ResponseEntity.ok(response);
    }
}
