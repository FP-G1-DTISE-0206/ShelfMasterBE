package com.DTISE.ShelfMasterBE.infrastructure.product.dto;

import com.DTISE.ShelfMasterBE.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    private String name;
    private BigDecimal price;

    public Product toEntity() {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
