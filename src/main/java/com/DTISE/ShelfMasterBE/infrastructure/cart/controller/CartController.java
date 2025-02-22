package com.DTISE.ShelfMasterBE.infrastructure.cart.controller;

import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CartCalculationRequest;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CartRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentResponse;
import com.DTISE.ShelfMasterBE.usecase.cart.CartCalculationUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartCalculationUsecase cartCalculationUsecase;

    public CartController(CartCalculationUsecase cartCalculationUsecase) {
        this.cartCalculationUsecase = cartCalculationUsecase;
    }

    @PostMapping("/calculate-total")
    public ResponseEntity<PaymentResponse> calculateTotal(@RequestBody CartRequest request) {
        PaymentResponse response = cartCalculationUsecase.execute(request);
        return ResponseEntity.ok(response);
    }
}
//@RestController
//@RequestMapping("/api/v1/cart")
//@RequiredArgsConstructor
//public class CartController {
//
//    private final CartCalculationUsecase cartCalculationUsecase;
//
//    @PostMapping("/calculate")
//    public ResponseEntity<PaymentResponse> calculateCart(@RequestBody CartCalculationRequest request) {
//        PaymentResponse paymentResponse = cartCalculationUsecase.execute(request);
//        return ResponseEntity.ok(paymentResponse);
//    }
//}
