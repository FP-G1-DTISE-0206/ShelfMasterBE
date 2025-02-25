package com.DTISE.ShelfMasterBE.infrastructure.cart.controller;


import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CreateCartItemRequest;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CreateCartItemResponse;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.GetCartResponse;
import com.DTISE.ShelfMasterBE.usecase.cart.AddToCartUsecase;
import com.DTISE.ShelfMasterBE.usecase.cart.GetCartUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final AddToCartUsecase addToCartUsecase;
    private final GetCartUsecase getCartUsecase;

    public CartController(AddToCartUsecase addToCartUsecase, GetCartUsecase getCartUsecase) {
        this.addToCartUsecase = addToCartUsecase;
        this.getCartUsecase = getCartUsecase;
    }


    @PostMapping
    public ResponseEntity<CreateCartItemResponse> addToCart(@RequestBody CreateCartItemRequest request) {
        System.out.println("Received request: " + request);
        return ResponseEntity.ok(addToCartUsecase.execute(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<GetCartResponse> getCart(@PathVariable Long userId) {
        GetCartResponse response = getCartUsecase.execute(userId);
        return ResponseEntity.ok(response);
    }

}
