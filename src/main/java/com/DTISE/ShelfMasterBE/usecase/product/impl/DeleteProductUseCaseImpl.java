package com.DTISE.ShelfMasterBE.usecase.product.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.product.DeleteProductUseCase;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class DeleteProductUseCaseImpl implements DeleteProductUseCase {
    private final ProductRepository productRepository;

    public DeleteProductUseCaseImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
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
