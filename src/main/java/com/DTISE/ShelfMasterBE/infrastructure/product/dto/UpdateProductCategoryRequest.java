package com.DTISE.ShelfMasterBE.infrastructure.product.dto;

import com.DTISE.ShelfMasterBE.entity.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductCategoryRequest {
    private Long id;
    private Long productId;
    private Long categoryId;

    public ProductCategory toEntity() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductId(productId);
        productCategory.setCategoryId(categoryId);
        return productCategory;
    }
}
