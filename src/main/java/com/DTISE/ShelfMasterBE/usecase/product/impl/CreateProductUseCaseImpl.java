package com.DTISE.ShelfMasterBE.usecase.product.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.exceptions.DuplicateProductNameException;
import com.DTISE.ShelfMasterBE.entity.Product;
import com.DTISE.ShelfMasterBE.entity.ProductCategory;
import com.DTISE.ShelfMasterBE.infrastructure.category.repository.CategoryRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.CreateProductCategoryResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.CreateProductRequest;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.CreateProductResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductCategoryRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.product.CreateProductUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreateProductUseCaseImpl implements CreateProductUseCase {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public CreateProductUseCaseImpl(
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
    public CreateProductResponse createProduct(CreateProductRequest req) {
        if (productRepository.existsByName(req.getName())) {
            throw new DuplicateProductNameException("Product name must be unique.");
        }
        Product createdProduct = productRepository.save(req.toEntity());
        return new CreateProductResponse(
                createdProduct.getId(),
                createdProduct.getName(),
                createdProduct.getPrice(),
                createProductCategories(createdProduct.getId(), req.getCategories()));
    }

    private List<CreateProductCategoryResponse> createProductCategories(
            Long productId,
            List<String> categories
    ) {
        return categories.stream()
                .map(this::getCategoryIdOrThrow)
                .map(categoryId -> createProductCategory(productId, categoryId))
                .map(this::mapProductCategoryResponse)
                .collect(Collectors.toList());
    }

    private Long getCategoryIdOrThrow(String categoryName) {
        Long categoryId = categoryRepository.findFirstIdByName(categoryName);
        if (categoryId == null) {
            throw new DataNotFoundException("Category with name " + categoryName + " does not exist.");
        }
        return categoryId;
    }

    private ProductCategory createProductCategory(Long productId, Long categoryId) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductId(productId);
        productCategory.setCategoryId(categoryId);
        return productCategoryRepository.save(productCategory);
    }

    private CreateProductCategoryResponse mapProductCategoryResponse(ProductCategory productCategory) {
        return new CreateProductCategoryResponse(
                productCategory.getId(),
                productCategory.getProductId(),
                productCategory.getCategoryId()
        );
    }
}
