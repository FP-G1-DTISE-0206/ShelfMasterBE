package com.DTISE.ShelfMasterBE.usecase.product.impl;

import com.DTISE.ShelfMasterBE.entity.Category;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.GetProductResponse;
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
    public Page<GetProductResponse> getProducts(Pageable pageable, String search) {
        return productRepository.findAllBySearch(search, pageable)
                .map(product -> new GetProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        mapProductCategoryResponse(product.getCategories())
                ));
    }

    private List<Long> mapProductCategoryResponse(
            Set<Category> categories) {
        List<Long> responses = new ArrayList<>();
        for (Category category : categories) {
            responses.add(category.getId());
        }
        return responses;
    }
}
