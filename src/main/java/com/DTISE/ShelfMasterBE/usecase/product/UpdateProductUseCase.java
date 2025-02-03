package com.DTISE.ShelfMasterBE.usecase.product;

import com.DTISE.ShelfMasterBE.infrastructure.product.dto.UpdateProductRequest;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.UpdateProductResponse;

public interface UpdateProductUseCase {
    UpdateProductResponse updateProduct(Long id, UpdateProductRequest req);
}
