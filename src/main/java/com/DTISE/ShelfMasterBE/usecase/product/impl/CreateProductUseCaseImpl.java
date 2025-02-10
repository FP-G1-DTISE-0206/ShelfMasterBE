package com.DTISE.ShelfMasterBE.usecase.product.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.exceptions.DuplicateProductNameException;
import com.DTISE.ShelfMasterBE.entity.Category;
import com.DTISE.ShelfMasterBE.entity.Product;
import com.DTISE.ShelfMasterBE.entity.ProductImage;
import com.DTISE.ShelfMasterBE.infrastructure.category.repository.CategoryRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.CategoryResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.CreateProductRequest;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.CreateProductResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductImageRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.product.CreateProductUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CreateProductUseCaseImpl implements CreateProductUseCase {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    public CreateProductUseCaseImpl(
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
    public CreateProductResponse createProduct(CreateProductRequest req) {
        if (productRepository.existsByName(req.getName())) {
            throw new DuplicateProductNameException("Product name must be unique.");
        }
        Product newProduct = productRepository.save(addCategory(req.toEntity(), req));
        addProductImages(newProduct.getId(), req.getImages());
        return mapCreatedProductResponse(newProduct);
    }

    private Product addCategory(Product product, CreateProductRequest req) {
        if (!req.getCategories().isEmpty()) {
            for (Long categoryId : req.getCategories()) {
                product.getCategories().add(getCategoryIdOrThrow(categoryId));
            }
        }
        return product;
    }

    private void addProductImages(Long productId, List<String> imageUrls) {
        if (imageUrls != null && !imageUrls.isEmpty()) {
            List<ProductImage> images = imageUrls.stream()
                    .map(url -> {
                        ProductImage image = new ProductImage();
                        image.setProductId(productId);
                        image.setImageUrl(url);
                        return image;
                    })
                    .collect(Collectors.toList());
            productImageRepository.saveAll(images);
        }
    }

    private Category getCategoryIdOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException(
                        "Category with ID " + categoryId + " does not exist."));
    }

    private CreateProductResponse mapCreatedProductResponse(Product createdProduct) {
        return new CreateProductResponse(
                createdProduct.getId(),
                createdProduct.getName());
    }

    private List<CategoryResponse> mapProductCategoryResponse(Set<Category> categories) {
        List<CategoryResponse> responses = new ArrayList<>();
        for (Category category : categories) {
            responses.add(new CategoryResponse(category.getId(), category.getName()));
        }
        return responses;
    }
}
