package com.DTISE.ShelfMasterBE.usecase.product.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.UpdateProductRequest;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.UpdateProductResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.product.UpdateProductUseCase;
import org.springframework.stereotype.Service;

@Service
public class UpdateProductUseCaseImpl implements UpdateProductUseCase {
    private final ProductRepository productRepository;

    public UpdateProductUseCaseImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public UpdateProductResponse updateProduct(Long id, UpdateProductRequest req) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(req.getName());
                    existingProduct.setPrice(req.getPrice());
                    return productRepository.save(existingProduct);
                })
                .map(updatedProduct -> new UpdateProductResponse(
                        updatedProduct.getId(),
                        updatedProduct.getName(),
                        updatedProduct.getPrice()))
                .orElseThrow(() -> new DataNotFoundException("There's no product with ID: " + id));
    }
}
