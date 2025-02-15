package com.DTISE.ShelfMasterBE.usecase.product;

import com.DTISE.ShelfMasterBE.infrastructure.product.dto.GetProductDetailResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.GetProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetProductsUseCase {
    Page<GetProductResponse> getProducts(Pageable pageable, String search);
    Page<GetProductResponse> getProductsByWarehouse(Pageable pageable, String search, Long warehouseId);
    GetProductDetailResponse getProductDetail(Long id);
}
