package com.DTISE.ShelfMasterBE.infrastructure.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {
    private Long id;
    private String name;
    private BigDecimal price;
}
