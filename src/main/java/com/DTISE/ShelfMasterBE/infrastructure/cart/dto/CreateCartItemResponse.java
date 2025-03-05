package com.DTISE.ShelfMasterBE.infrastructure.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCartItemResponse {

    private Long cartId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal weight;
    private String sku;
    private Boolean isProcessed;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;


}
