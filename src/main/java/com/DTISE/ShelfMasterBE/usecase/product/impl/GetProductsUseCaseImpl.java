package com.DTISE.ShelfMasterBE.usecase.product.impl;

import com.DTISE.ShelfMasterBE.entity.Product;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.GetProductCategoryResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.GetProductResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.UpdateProductCategoryRequest;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.UpdateProductCategoryResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.product.GetProductsUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
                        product.getPrice(),
                        getProductCategories(product.getId())
            ));
    }

    private List<GetProductCategoryResponse> getProductCategories(Long productId) {
        return null;
    }
}
