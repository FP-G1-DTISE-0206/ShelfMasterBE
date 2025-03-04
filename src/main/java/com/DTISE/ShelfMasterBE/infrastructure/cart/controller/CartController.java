package com.DTISE.ShelfMasterBE.infrastructure.cart.controller;


import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.*;
import com.DTISE.ShelfMasterBE.usecase.cart.AddToCartUsecase;
import com.DTISE.ShelfMasterBE.usecase.cart.GetCartUsecase;
import com.DTISE.ShelfMasterBE.usecase.cart.RemoveCartItemUsecase;
import com.DTISE.ShelfMasterBE.usecase.cart.UpdateCartItemUsecase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final AddToCartUsecase addToCartUsecase;
    private final GetCartUsecase getCartUsecase;
    private final UpdateCartItemUsecase updateCartItemUsecase;
    private final RemoveCartItemUsecase removeCartItemUsecase;

    public CartController(AddToCartUsecase addToCartUsecase, GetCartUsecase getCartUsecase, UpdateCartItemUsecase updateCartItemUsecase, RemoveCartItemUsecase removeCartItemUsecase) {
        this.addToCartUsecase = addToCartUsecase;
        this.getCartUsecase = getCartUsecase;
        this.updateCartItemUsecase = updateCartItemUsecase;
        this.removeCartItemUsecase = removeCartItemUsecase;
    }


    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody CreateCartItemRequest request) {
        System.out.println("Received request: " + request);
        return ApiResponse.successfulResponse("Item added to cart successfully", addToCartUsecase.execute(request));
    }


    @GetMapping
    public ResponseEntity<?> getCart() {
        String email = Claims.getEmailFromJwt();
        GetCartResponse response = getCartUsecase.execute(email);
        return ApiResponse.successfulResponse("Cart retrieved successfully", response);
    }


    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Long cartId) {
        String email = Claims.getEmailFromJwt();
        removeCartItemUsecase.execute(email, cartId);
        return ApiResponse.successfulResponse("Cart item removed successfully");
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable Long cartId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        UpdateCartItemResponse response = updateCartItemUsecase.execute(cartId, request);
        return ApiResponse.successfulResponse("Cart item updated successfully", response);
    }

}
