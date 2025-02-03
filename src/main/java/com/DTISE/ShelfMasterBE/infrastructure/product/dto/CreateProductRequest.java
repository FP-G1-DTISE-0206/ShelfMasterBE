package com.DTISE.ShelfMasterBE.infrastructure.product.dto;

import com.DTISE.ShelfMasterBE.entity.Category;
import com.DTISE.ShelfMasterBE.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    private String name;
    private BigDecimal price;
    private List<Long> categories;

    public Product toEntity() {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        Set<Category> categories = new HashSet<>();
        product.setCategories(categories);
        return product;
    }
}
