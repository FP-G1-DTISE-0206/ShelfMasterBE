package com.DTISE.ShelfMasterBE.infrastructure.cart.dto;

import lombok.Data;

import java.math.BigInteger;
import java.time.OffsetDateTime;

@Data
public class UpdateCartItemResponse {
    private BigInteger cartId;
    private BigInteger productId;
    private String productName;
    private Integer quantity;
    private Boolean isProcessed;
    private OffsetDateTime updatedAt;
}
