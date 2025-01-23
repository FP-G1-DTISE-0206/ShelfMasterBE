package com.DTISE.ShelfMasterBE.usecase.product.impl;

import com.DTISE.ShelfMasterBE.entity.Product;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.GetProductResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.product.GetProductsUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GetProductsUseCaseImpl implements GetProductsUseCase {
    private final ProductRepository productRepository;

    public GetProductsUseCaseImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<GetProductResponse> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(product -> new GetProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getPrice()
            ));
    }
}
