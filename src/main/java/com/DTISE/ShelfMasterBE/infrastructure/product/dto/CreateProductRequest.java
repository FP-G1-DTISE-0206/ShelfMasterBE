package com.DTISE.ShelfMasterBE.infrastructure.product.dto;

import com.DTISE.ShelfMasterBE.entity.Category;
import com.DTISE.ShelfMasterBE.entity.Product;
import com.DTISE.ShelfMasterBE.entity.ProductImage;
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
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal weight;
    private List<Long> categories;
    private List<String> images;

    public Product toEntity() {
        Product product = new Product();
        product.setSku(sku);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setWeight(weight);
        Set<Category> categories = new HashSet<>();
        product.setCategories(categories);
        return product;
    }
}
