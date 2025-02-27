package com.DTISE.ShelfMasterBE.usecase.product.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.tools.PermissionUtils;
import com.DTISE.ShelfMasterBE.common.tools.ProductMapper;
import com.DTISE.ShelfMasterBE.entity.Category;
import com.DTISE.ShelfMasterBE.entity.ProductImage;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.*;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.product.GetProductsUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetProductsUseCaseImpl implements GetProductsUseCase {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public GetProductsUseCaseImpl(
            ProductRepository productRepository,
            UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<GetProductResponse> getProducts(
            Pageable pageable,
            String search,
            List<Long> categories,
            Long warehouseId) {
        validateUserAccess(warehouseId);
        return productRepository.findAllBySearchAndOrWarehouseId(search, categories, pageable)
                .map(product -> ProductMapper.mapGetProductResponse(product, warehouseId));
    }

    @Override
    public GetProductDetailResponse getProductDetail(Long id) {
        return productRepository.findById(id)
                .map(product -> new GetProductDetailResponse(
                        product.getId(),
                        product.getSku(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getWeight(),
                        mapProductCategoryResponse(product.getCategories()),
                        mapProductImageResponse(product.getImages())
                ))
                .orElseThrow(() -> new DataNotFoundException(
                        "Product not found with id: " + id));
    }

    private void validateUserAccess(Long warehouseId) {
        if(warehouseId == null) return;
        User user = userRepository.findById(Claims.getUserIdFromJwt())
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        if (PermissionUtils.isSuperAdmin(user)) return;
        PermissionUtils.isAdminOfCurrentWarehouse(user, warehouseId);
    }

    private List<CategoryResponse> mapProductCategoryResponse(
            Set<Category> categories) {
        List<CategoryResponse> responses = new ArrayList<>();
        for (Category category : categories) {
            responses.add(new CategoryResponse(category.getId(), category.getName()));
        }
        return responses;
    }

    private List<ProductImageResponse> mapProductImageResponse(
            Set<ProductImage> images) {
        List<ProductImageResponse> responses = new ArrayList<>();
        for (ProductImage image : images) {
            responses.add(new ProductImageResponse(image.getId(), image.getImageUrl()));
        }
        return responses;
    }
}
