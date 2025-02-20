package com.DTISE.ShelfMasterBE.usecase.productMutation.impl;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.tools.PermissionUtils;
import com.DTISE.ShelfMasterBE.entity.*;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.ProductMutationResponse;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.ProductMutationRepository;
import com.DTISE.ShelfMasterBE.usecase.productMutation.GetProductMutationUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetProductMutationUseCaseImpl implements GetProductMutationUseCase {
    private final ProductMutationRepository productMutationRepository;
    private final UserRepository userRepository;

    public GetProductMutationUseCaseImpl(
            ProductMutationRepository productMutationRepository,
            UserRepository userRepository
    ) {
        this.productMutationRepository = productMutationRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Page<ProductMutationResponse> getProductMutations(Pageable pageable, String search, Long warehouseId) {
        validateUserAccess(warehouseId);
        return productMutationRepository.getAllBySearchAndWarehouseId(
                        search, pageable, warehouseId,
                        MutationEntityType.WAREHOUSE);
    }

    private void validateUserAccess(Long warehouseId) {
        User user = userRepository.findById(Claims.getUserIdFromJwt())
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        if (PermissionUtils.isSuperAdmin(user)) return;
        PermissionUtils.isAdminOfCurrentWarehouse(user, warehouseId);
    }
}
