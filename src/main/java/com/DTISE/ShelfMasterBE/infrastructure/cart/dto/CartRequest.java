package com.DTISE.ShelfMasterBE.infrastructure.cart.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartRequest {
    private List<CartItemDTO> cartItems;

    @Data
    public static class CartItemDTO {
        private Long id;
        private String name;
        private int quantity;
        private double price;
    }
}