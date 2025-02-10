package com.DTISE.ShelfMasterBE.infrastructure.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageResponse {
    private Long id;
    private String imageUrl;
}
