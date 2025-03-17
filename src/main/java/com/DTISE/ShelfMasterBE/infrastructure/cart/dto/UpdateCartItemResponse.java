package com.DTISE.ShelfMasterBE.infrastructure.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemResponse {
    private Long cartId;
    private Long productId;
    private String productName;
    private Long quantity;
    private BigDecimal weight;
    private String sku;
    private Boolean isProcessed;
    private OffsetDateTime updatedAt;
}
