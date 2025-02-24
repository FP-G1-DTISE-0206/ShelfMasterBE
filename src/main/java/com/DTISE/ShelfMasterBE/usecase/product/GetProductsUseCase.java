package com.DTISE.ShelfMasterBE.usecase.product;

import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.GetProductDetailResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.GetProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GetProductsUseCase {
    Page<GetProductResponse> getProducts(
            Pageable pageable,
            String search,
            List<Long> categories,
            Long warehouseId);

    GetProductDetailResponse getProductDetail(Long id);
}
