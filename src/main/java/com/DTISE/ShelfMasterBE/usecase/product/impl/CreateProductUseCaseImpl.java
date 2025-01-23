package com.DTISE.ShelfMasterBE.usecase.product.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DuplicateProductNameException;
import com.DTISE.ShelfMasterBE.entity.Product;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.CreateProductRequest;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.CreateProductResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.product.CreateProductUseCase;
import org.springframework.stereotype.Service;

@Service
public class CreateProductUseCaseImpl implements CreateProductUseCase {
    private final ProductRepository productRepository;

    public CreateProductUseCaseImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public CreateProductResponse createProduct(CreateProductRequest req) {
        if (productRepository.existsByName(req.getName())) {
            throw new DuplicateProductNameException("Product name must be unique.");
        }
        Product createdProduct = productRepository.save(req.toEntity());
        return new CreateProductResponse(
                createdProduct.getId(),
                createdProduct.getName(),
                createdProduct.getPrice());
    }
}
