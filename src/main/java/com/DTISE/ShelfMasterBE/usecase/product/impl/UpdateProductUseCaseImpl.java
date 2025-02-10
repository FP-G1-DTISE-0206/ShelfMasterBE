package com.DTISE.ShelfMasterBE.usecase.product.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.exceptions.DuplicateProductNameException;
import com.DTISE.ShelfMasterBE.entity.Category;
import com.DTISE.ShelfMasterBE.entity.Product;
import com.DTISE.ShelfMasterBE.entity.ProductImage;
import com.DTISE.ShelfMasterBE.infrastructure.category.repository.CategoryRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.*;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductImageRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.product.UpdateProductUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UpdateProductUseCaseImpl implements UpdateProductUseCase {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    public UpdateProductUseCaseImpl(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ProductImageRepository productImageRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productImageRepository = productImageRepository;
    }

    @Override
    @Transactional
    public UpdateProductResponse updateProduct(Long id, UpdateProductRequest req) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    checkProductNameAvailable(id, req);
                    existingProduct.setName(req.getName());
                    existingProduct.setPrice(req.getPrice());
                    existingProduct.setUpdatedAt(OffsetDateTime.now());
                    updateCategories(existingProduct, req);
                    updateProductImages(existingProduct, req);
                    return productRepository.save(existingProduct);
                })
                .map(this::mapUpdateProductResponse)
                .orElseThrow(() -> new DataNotFoundException(
                        "There's no product with ID: " + id));
    }

    private void checkProductNameAvailable(Long id, UpdateProductRequest req) {
        Optional<Product> existingProduct = productRepository.getFirstByName(req.getName());
        if (existingProduct.isPresent() && !existingProduct.get().getId().equals(req.getId())) {
            throw new DuplicateProductNameException("Product with this name already exists");
        }
    }

    private UpdateProductResponse mapUpdateProductResponse(Product updatedProduct) {
        return new UpdateProductResponse(
                updatedProduct.getId(),
                updatedProduct.getSku(),
                updatedProduct.getName(),
                updatedProduct.getDescription(),
                updatedProduct.getPrice(),
                updatedProduct.getWeight(),
                mapProductCategoryResponse(updatedProduct.getCategories()),
                mapProductImageResponse(updatedProduct.getImages()));
    }

    private void updateProductImages(Product updatingProduct, UpdateProductRequest req) {
        Set<ProductImage> updatingImages = updatingProduct.getImages();
        List<String> newImages = req.getImages();

        if (newImages == null || newImages.isEmpty()) {
            markImagesAsDeleted(updatingImages);
        } else if (updatingImages.size() > newImages.size()) {
            updateAndDeleteExtraImages(updatingImages, newImages);
        } else {
            updateAndAddNewImages(updatingProduct, updatingImages, newImages);
        }

        productImageRepository.saveAll(updatingImages);
    }

    private void markImagesAsDeleted(Set<ProductImage> images) {
        images.forEach(img -> img.setDeletedAt(OffsetDateTime.now()));
    }

    private void updateAndDeleteExtraImages(Set<ProductImage> images, List<String> newImages) {
        AtomicInteger index = new AtomicInteger();
        images.forEach(img -> {
            if (index.get() < newImages.size()) {
                img.setImageUrl(newImages.get(index.getAndIncrement()));
            } else {
                img.setDeletedAt(OffsetDateTime.now());
            }
        });
    }

    private void updateAndAddNewImages(Product product, Set<ProductImage> images, List<String> newImages) {
        AtomicInteger index = new AtomicInteger();
        images.forEach(img -> img.setImageUrl(newImages.get(index.getAndIncrement())));
        for (int i = index.get(); i < newImages.size(); i++) {
            ProductImage newImage = new ProductImage();
            newImage.setProductId(product.getId());
            newImage.setImageUrl(newImages.get(i));
            images.add(newImage);
        }
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

    private List<ProductImageResponse> mapProductImageResponse(
            Set<ProductImage> images) {
        List<ProductImageResponse> responses = new ArrayList<>();
        for (ProductImage image : images) {
            responses.add(new ProductImageResponse(image.getId(), image.getImageUrl()));
        }
        return responses;
    }
}
