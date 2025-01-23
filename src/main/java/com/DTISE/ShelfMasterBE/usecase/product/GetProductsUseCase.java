package com.DTISE.ShelfMasterBE.usecase.product;

import com.DTISE.ShelfMasterBE.infrastructure.product.dto.GetProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetProductsUseCase {
    Page<GetProductResponse> getProducts(Pageable pageable);
}
