package com.DTISE.ShelfMasterBE.common.tools;

import com.DTISE.ShelfMasterBE.entity.Product;
import com.DTISE.ShelfMasterBE.entity.ProductImage;
import com.DTISE.ShelfMasterBE.entity.ProductStock;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.GetProductResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.ProductImageResponse;

import java.util.Set;

public class ProductMapper {
    public static GetProductResponse mapGetProductResponse(Product product) {
        return new GetProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                getFirstImage(product.getImages()),
                sumProductQuantity(product.getStock())
        );
    }

    private static ProductImageResponse getFirstImage(Set<ProductImage> images) {
        return images == null || images.isEmpty()
                ? null
                : images.stream()
                .findFirst()
                .map(image -> new ProductImageResponse(image.getId(), image.getImageUrl()))
                .orElse(null);
    }

    private static Integer sumProductQuantity(Set<ProductStock> stock) {
        return stock == null ? 0 : stock.stream()
                .map(ProductStock::getQuantity)
                .reduce(0, Integer::sum);
    }
}
