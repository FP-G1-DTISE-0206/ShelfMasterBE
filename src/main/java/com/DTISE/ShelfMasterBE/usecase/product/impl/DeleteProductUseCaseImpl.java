package com.DTISE.ShelfMasterBE.usecase.product.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductCategoryRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.product.DeleteProductUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class DeleteProductUseCaseImpl implements DeleteProductUseCase {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public DeleteProductUseCaseImpl(
            ProductRepository productRepository,
            ProductCategoryRepository productCategoryRepository
    ) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        productRepository
                .findById(id)
                .map(existingProduct -> {
                    existingProduct.setDeletedAt(OffsetDateTime.now());
                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new DataNotFoundException("There's no product with ID: " + id));
    }
}
