package com.DTISE.ShelfMasterBE.usecase.product.impl;

import com.DTISE.ShelfMasterBE.entity.Product;
import com.DTISE.ShelfMasterBE.entity.ProductCategory;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.GetProductCategoryResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.GetProductResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.UpdateProductCategoryRequest;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.UpdateProductCategoryResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.product.GetProductsUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
                        mapProductCategoryResponse(product.getProductCategories())
            ));
    }

    private List<GetProductCategoryResponse> mapProductCategoryResponse(
            Set<ProductCategory> productCategories) {
        List<GetProductCategoryResponse> responses = new ArrayList<>();
        for (ProductCategory category : productCategories) {
            responses.add(new GetProductCategoryResponse(
                    category.getId(),
                    category.getProductId(),
                    category.getCategoryId()
            ));
        }
        return responses;
    }
}
