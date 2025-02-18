package com.DTISE.ShelfMasterBE.infrastructure.cart.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long productId;
    private Integer quantity;
}
