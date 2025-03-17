package com.DTISE.ShelfMasterBE.infrastructure.cart.controller;


import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.*;
import com.DTISE.ShelfMasterBE.usecase.cart.AddToCartUsecase;
import com.DTISE.ShelfMasterBE.usecase.cart.GetCartUsecase;
import com.DTISE.ShelfMasterBE.usecase.cart.RemoveCartItemUsecase;
import com.DTISE.ShelfMasterBE.usecase.cart.UpdateCartItemUsecase;
import com.DTISE.ShelfMasterBE.usecase.productMutation.OrderMutationUseCase;
import com.DTISE.ShelfMasterBE.usecase.productMutation.impl.OrderMutationUseCaseImpl;
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
    private final OrderMutationUseCase orderMutationUseCase;


    public CartController(AddToCartUsecase addToCartUsecase, GetCartUsecase getCartUsecase, UpdateCartItemUsecase updateCartItemUsecase, RemoveCartItemUsecase removeCartItemUsecase, OrderMutationUseCase orderMutationUseCase) {
        this.addToCartUsecase = addToCartUsecase;
        this.getCartUsecase = getCartUsecase;
        this.updateCartItemUsecase = updateCartItemUsecase;
        this.removeCartItemUsecase = removeCartItemUsecase;
        this.orderMutationUseCase = orderMutationUseCase;

    }


    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody CreateCartItemRequest request) {
        String email = Claims.getEmailFromJwt();

        return ApiResponse.successfulResponse("Item added to cart successfully", addToCartUsecase.execute(request, email));
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

    @GetMapping("/product-stock/{id}")
    public ResponseEntity<?> getTotalProductStock(@PathVariable Long id) {
        return ApiResponse.successfulResponse(
                "Total product stock retrieved successfully",
                orderMutationUseCase.getProductTotalStock(id)
        );
    }

}
