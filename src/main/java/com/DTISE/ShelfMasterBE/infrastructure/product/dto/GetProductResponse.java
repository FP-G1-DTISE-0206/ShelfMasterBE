package com.DTISE.ShelfMasterBE.infrastructure.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private List<GetProductCategoryResponse> categories;
}
