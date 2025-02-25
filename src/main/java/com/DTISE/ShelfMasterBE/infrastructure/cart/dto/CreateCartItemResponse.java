package com.DTISE.ShelfMasterBE.infrastructure.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCartItemResponse {

    private BigInteger cartId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Boolean isProcessed;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
