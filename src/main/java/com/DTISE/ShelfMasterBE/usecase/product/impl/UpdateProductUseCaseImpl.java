package com.DTISE.ShelfMasterBE.usecase.product.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
//import com.DTISE.ShelfMasterBE.entity.ProductCategory;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.*;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductCategoryRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.product.UpdateProductUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
//import java.util.stream.Collectors;

@Service
public class UpdateProductUseCaseImpl implements UpdateProductUseCase {
    private final ProductRepository productRepository;
    //private final ProductCategoryRepository productCategoryRepository;

    public UpdateProductUseCaseImpl(
            ProductRepository productRepository//,
            //ProductCategoryRepository productCategoryRepository
    ) {
        this.productRepository = productRepository;
        //this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    @Transactional
    public UpdateProductResponse updateProduct(Long id, UpdateProductRequest req) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(req.getName());
                    existingProduct.setPrice(req.getPrice());
                    existingProduct.setUpdatedAt(OffsetDateTime.now());
                    return productRepository.save(existingProduct);
                })
                .map(updatedProduct -> new UpdateProductResponse(
                        updatedProduct.getId(),
                        updatedProduct.getName(),
                        updatedProduct.getPrice(),
                        updateProductCategories(
                                id,
                                req.getCategories(),
                                req.getRemovedCategories()
                        )))
                .orElseThrow(() -> new DataNotFoundException(
                        "There's no product with ID: " + id));
    }

    private List<UpdateProductCategoryResponse> updateProductCategories(
            Long productId,
            List<UpdateProductCategoryRequest> productCategories,
            List<UpdateProductCategoryRequest> removedProductCategory
    ) {
        return null;/* productCategories.stream()
                .map(this::getProductCategoryOrThrow)
                .map(this::mapProductCategoryResponse)
                .collect(Collectors.toList());*/
    }

    /*private ProductCategory getProductCategoryOrThrow(UpdateProductCategoryRequest productCategory) {
        productCategoryRepository
                .findById(productCategory.getId())
                    .map(existingCategory -> {
                        existingCategory.setDeletedAt(OffsetDateTime.now());
                        return categoryRepository.save(existingCategory);
                    })
                    .orElseThrow(() -> new DataNotFoundException("There's no category with ID: " + id));
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
    }*/
}
