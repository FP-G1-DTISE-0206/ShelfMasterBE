package com.DTISE.ShelfMasterBE.usecase.product.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.exceptions.DuplicateProductNameException;
import com.DTISE.ShelfMasterBE.entity.Category;
import com.DTISE.ShelfMasterBE.entity.Product;
import com.DTISE.ShelfMasterBE.infrastructure.category.repository.CategoryRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.CreateProductRequest;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.CreateProductResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.product.CreateProductUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CreateProductUseCaseImpl implements CreateProductUseCase {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public CreateProductUseCaseImpl(
            ProductRepository productRepository,
            CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest req) {
        if (productRepository.existsByName(req.getName())) {
            throw new DuplicateProductNameException("Product name must be unique.");
        }
        Product newProduct = addCategory(req.toEntity(), req);
        return mapCreatedProductResponse(productRepository.save(newProduct));
    }

    private Product addCategory(Product product, CreateProductRequest req) {
        if (!req.getCategories().isEmpty()) {
            for (Long categoryId : req.getCategories()) {
                product.getCategories().add(getCategoryIdOrThrow(categoryId));
            }
        }
        return product;
    }

    private Category getCategoryIdOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException(
                        "Category with ID " + categoryId + " does not exist."));
    }

    private CreateProductResponse mapCreatedProductResponse(Product createdProduct) {
        return new CreateProductResponse(
                createdProduct.getId(),
                createdProduct.getName(),
                createdProduct.getPrice(),
                mapProductCategoryResponse(createdProduct.getCategories()));
    }

    private List<Long> mapProductCategoryResponse(Set<Category> categories) {
        List<Long> responses = new ArrayList<>();
        for (Category category : categories) {
            responses.add(category.getId());
        }
        return responses;
    }
}
