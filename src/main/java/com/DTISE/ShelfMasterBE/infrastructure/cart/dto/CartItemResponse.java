package com.DTISE.ShelfMasterBE.infrastructure.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private Long cartId;
    private Long productId;
    private String productName;
    private Long quantity;
    private BigDecimal weight;
    private String sku;
    private Boolean isProcessed;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
