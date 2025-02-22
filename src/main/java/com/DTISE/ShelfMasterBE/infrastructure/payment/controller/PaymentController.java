package com.DTISE.ShelfMasterBE.infrastructure.payment.controller;

import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentResponse;
import com.DTISE.ShelfMasterBE.usecase.payment.MidtransServiceUsecase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final MidtransServiceUsecase midtransServiceUsecase;
//    @PostMapping("/create")
//    public ResponseEntity<?> createTransaction(@Valid @RequestBody PaymentRequest request) {
//        System.out.println("💰 Received Payment Request: " + request);
//        return ResponseEntity.ok(midtransServiceUsecase.createTransaction(request));
//    }


    @PostMapping("/create")
    public ApiResponse<PaymentResponse> createTransaction(@Valid @RequestBody PaymentRequest request) {
        System.out.println("Received Payment Request: " + request);
        PaymentResponse response = midtransServiceUsecase.createTransaction(request);
//        return ResponseEntity.ok(response);
        return ApiResponse.successfulResponse("Transaction created successfully", response).getBody();
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

//import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
//import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentRequest;
//import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentResponse;
//import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentStatusResponse;
//import com.DTISE.ShelfMasterBE.usecase.payment.CheckPaymentStatusUsecase;
//import com.DTISE.ShelfMasterBE.usecase.payment.CreatePaymentUsecase;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;