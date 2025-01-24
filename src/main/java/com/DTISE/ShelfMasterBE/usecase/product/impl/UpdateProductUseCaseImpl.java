package com.DTISE.ShelfMasterBE.usecase.product.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.entity.Product;
import com.DTISE.ShelfMasterBE.entity.ProductCategory;
import com.DTISE.ShelfMasterBE.infrastructure.category.repository.CategoryRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.*;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductCategoryRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.product.UpdateProductUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UpdateProductUseCaseImpl implements UpdateProductUseCase {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public UpdateProductUseCaseImpl(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ProductCategoryRepository productCategoryRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    @Transactional
    public UpdateProductResponse updateProduct(Long id, UpdateProductRequest req) {
        updateProductCategories(id, req.getCategories(), req.getRemovedCategories());
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(req.getName());
                    existingProduct.setPrice(req.getPrice());
                    existingProduct.setUpdatedAt(OffsetDateTime.now());
                    return productRepository.save(existingProduct);
                })
                .map(this::mapToProductResponse)
                .orElseThrow(() -> new DataNotFoundException(
                        "There's no product with ID: " + id));
    }

    private UpdateProductResponse mapToProductResponse(Product updatedProduct) {
        return new UpdateProductResponse(
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getPrice(),
                mapProductCategoryResponse(updatedProduct.getProductCategories()));
    }

    private void updateProductCategories(
            Long productId,
            List<UpdateProductCategoryRequest> productCategories,
            List<UpdateProductCategoryRequest> removedProductCategories
    ) {
        for (UpdateProductCategoryRequest productCategory : productCategories) {
            if (productCategory.getId() != null) {
                updateProductCategory(productId, productCategory);
            } else {
                createProductCategory(productId, productCategory);
            }
        }

        for (UpdateProductCategoryRequest categoryReq : removedProductCategories) {
            if (categoryReq.getId() != null) {
                removeProductCategory(categoryReq);
            }
        }
    }

    private void updateProductCategory(Long productId, UpdateProductCategoryRequest productCategory) {
        categoryRepository.findById(productCategory.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Category with ID: " + productCategory.getCategoryId() + " not found."));

        ProductCategory existingProductCategory = productCategoryRepository.findById(productCategory.getId())
                .orElseThrow(() -> new DataNotFoundException(
                        "ProductCategory with ID: " + productCategory.getId() + " not found for Product ID: " + productId));

        existingProductCategory.setProductId(productId);
        existingProductCategory.setCategoryId(productCategory.getCategoryId());
        existingProductCategory.setUpdatedAt(OffsetDateTime.now());
        productCategoryRepository.save(existingProductCategory);
    }

    private void createProductCategory(Long productId, UpdateProductCategoryRequest productCategory) {
        categoryRepository.findById(productCategory.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Category with ID: " + productCategory.getCategoryId() + " not found."));

        ProductCategory newProductCategory = productCategory.toEntity();
        newProductCategory.setProductId(productId);
        productCategoryRepository.save(newProductCategory);
    }

    private void removeProductCategory(UpdateProductCategoryRequest categoryReq) {
        productCategoryRepository.findById(categoryReq.getId())
                .ifPresent(existingProductCategory -> {
                    existingProductCategory.setDeletedAt(OffsetDateTime.now());
                    productCategoryRepository.save(existingProductCategory);
                });
    }

    private List<UpdateProductCategoryResponse> mapProductCategoryResponse(
            Set<ProductCategory> productCategories) {
        List<UpdateProductCategoryResponse> responses = new ArrayList<>();
        for (ProductCategory category : productCategories) {
            responses.add(new UpdateProductCategoryResponse(
                    category.getId(),
                    category.getProductId(),
                    category.getCategoryId()
            ));
        }
        return responses;
    }
}
