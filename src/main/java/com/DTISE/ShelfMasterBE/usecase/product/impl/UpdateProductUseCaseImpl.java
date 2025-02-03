package com.DTISE.ShelfMasterBE.usecase.product.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.entity.Category;
import com.DTISE.ShelfMasterBE.entity.Product;
import com.DTISE.ShelfMasterBE.infrastructure.category.repository.CategoryRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.*;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.product.UpdateProductUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UpdateProductUseCaseImpl implements UpdateProductUseCase {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public UpdateProductUseCaseImpl(
            ProductRepository productRepository,
            CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public UpdateProductResponse updateProduct(Long id, UpdateProductRequest req) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(req.getName());
                    existingProduct.setPrice(req.getPrice());
                    existingProduct.setUpdatedAt(OffsetDateTime.now());
                    updateCategories(existingProduct, req);
                    return productRepository.save(existingProduct);
                })
                .map(this::mapUpdateProductResponse)
                .orElseThrow(() -> new DataNotFoundException(
                        "There's no product with ID: " + id));
    }

    private UpdateProductResponse mapUpdateProductResponse(Product updatedProduct) {
        return new UpdateProductResponse(
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getPrice(),
                mapProductCategoryResponse(updatedProduct.getCategories()));
    }

    private void updateCategories(Product updatingProduct, UpdateProductRequest req) {
        if (req.getCategories() == null || req.getCategories().isEmpty()) {
            updatingProduct.setCategories(null);
        } else {
            updatingProduct.getCategories().clear();
            for (Long categoryId : req.getCategories()) {
                updatingProduct.getCategories().add(getCategoryIdOrThrow(categoryId));
            }
        }
    }

    private Category getCategoryIdOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException(
                        "Category with ID " + categoryId + " does not exist."));
    }

    private List<CategoryResponse> mapProductCategoryResponse(
            Set<Category> categories) {
        List<CategoryResponse> responses = new ArrayList<>();
        for (Category category : categories) {
            responses.add(new CategoryResponse(category.getId(), category.getName()));
        }
        return responses;
    }
}
