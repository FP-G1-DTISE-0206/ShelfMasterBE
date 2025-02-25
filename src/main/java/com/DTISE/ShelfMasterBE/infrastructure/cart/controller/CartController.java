package com.DTISE.ShelfMasterBE.infrastructure.cart.controller;


import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CreateCartItemRequest;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CreateCartItemResponse;
import com.DTISE.ShelfMasterBE.usecase.cart.AddToCartUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final AddToCartUsecase addToCartUsecase;

    public CartController(AddToCartUsecase addToCartUsecase) {
        this.addToCartUsecase = addToCartUsecase;
    }

    @PostMapping
    public ResponseEntity<CreateCartItemResponse> addToCart(@RequestBody CreateCartItemRequest request) {
        System.out.println("Received request: " + request);
        return ResponseEntity.ok(addToCartUsecase.execute(request));
    }

}
