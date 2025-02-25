package com.DTISE.ShelfMasterBE.infrastructure.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemResponse {
    private BigInteger cartId;
    private BigInteger productId;
    private String productName;
    private Integer quantity;
    private Boolean isProcessed;
    private OffsetDateTime updatedAt;
}
