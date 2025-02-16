package com.DTISE.ShelfMasterBE.usecase.product.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.tools.ProductMapper;
import com.DTISE.ShelfMasterBE.entity.Category;
import com.DTISE.ShelfMasterBE.entity.ProductImage;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.*;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.product.GetProductsUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetProductsUseCaseImpl implements GetProductsUseCase {
    private final ProductRepository productRepository;

    public GetProductsUseCaseImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<GetProductResponse> getProducts(Pageable pageable, String search, List<Long> categories) {
        return productRepository.findAllBySearchAndCategoryIds(search, categories, pageable)
                .map(ProductMapper::mapGetProductResponse);
    }

    @Override
    public Page<GetProductResponse> getProductsByWarehouse(Pageable pageable, String search, Long warehouseId) {
        return productRepository.findAllBySearchAndWarehouseId(search, pageable, warehouseId)
                .map(ProductMapper::mapGetProductResponse);
    }

    @Override
    public GetProductDetailResponse getProductDetail(Long id) {
        return productRepository.findById(id)
                .map(product -> new GetProductDetailResponse(
                        product.getId(),
                        product.getSku(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getWeight(),
                        mapProductCategoryResponse(product.getCategories()),
                        mapProductImageResponse(product.getImages())
                ))
                .orElseThrow(() -> new DataNotFoundException(
                        "Product not found with id: " + id));
    }

    private List<CategoryResponse> mapProductCategoryResponse(
            Set<Category> categories) {
        List<CategoryResponse> responses = new ArrayList<>();
        for (Category category : categories) {
            responses.add(new CategoryResponse(category.getId(), category.getName()));
        }
        return responses;
    }

    private List<ProductImageResponse> mapProductImageResponse(
            Set<ProductImage> images) {
        List<ProductImageResponse> responses = new ArrayList<>();
        for (ProductImage image : images) {
            responses.add(new ProductImageResponse(image.getId(), image.getImageUrl()));
        }
        return responses;
    }
}
