package com.DTISE.ShelfMasterBE.infrastructure.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private Long cartId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Boolean isProcessed;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
