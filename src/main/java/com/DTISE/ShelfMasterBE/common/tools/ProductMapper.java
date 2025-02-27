package com.DTISE.ShelfMasterBE.common.tools;

import com.DTISE.ShelfMasterBE.entity.Product;
import com.DTISE.ShelfMasterBE.entity.ProductImage;
import com.DTISE.ShelfMasterBE.entity.ProductStock;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.GetProductResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.ProductImageResponse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ProductMapper {
    public static GetProductResponse mapGetProductResponse(Product product, Long warehouseId) {
        return new GetProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                getFirstImage(product.getImages()),
                sumProductQuantity(product.getStock(), warehouseId)
        );
    }

    private static ProductImageResponse getFirstImage(Set<ProductImage> images) {
        if (images == null || images.isEmpty()) return null;
        List<ProductImage> imageList = new ArrayList<>(images);
        imageList.sort(Comparator.comparing(ProductImage::getId, Comparator.nullsLast(Long::compareTo)));
        ProductImage firstImage = imageList.getFirst();
        return new ProductImageResponse(firstImage.getId(), firstImage.getImageUrl());
    }

    private static Long sumProductQuantity(Set<ProductStock> stock, Long warehouseId) {
        return (stock == null || stock.isEmpty()) ? 0L : stock.stream()
                .filter(s -> warehouseId == null || s.getWarehouseId().equals(warehouseId))
                .map(s -> s.getQuantity() == null ? 0L : s.getQuantity())
                .reduce(0L, Long::sum);
    }
}
