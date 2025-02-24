package com.DTISE.ShelfMasterBE.usecase.productMutation.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.tools.PermissionUtils;
import com.DTISE.ShelfMasterBE.entity.ProductMutation;
import com.DTISE.ShelfMasterBE.entity.ProductMutationLog;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.ProductMutationLogResponse;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.ProductMutationLogRepository;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.ProductMutationRepository;
import com.DTISE.ShelfMasterBE.usecase.productMutation.GetDetailLogsUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetDetailLogsUseCaseImpl implements GetDetailLogsUseCase {
    private final ProductMutationLogRepository productMutationLogRepository;
    private final UserRepository userRepository;
    private final ProductMutationRepository mutationRepository;

    public GetDetailLogsUseCaseImpl(
            ProductMutationLogRepository productMutationLogRepository,
            UserRepository userRepository,
            ProductMutationRepository mutationRepository) {
        this.productMutationLogRepository = productMutationLogRepository;
        this.userRepository = userRepository;
        this.mutationRepository = mutationRepository;
    }

    @Override
    public List<ProductMutationLogResponse> getLogs(Long productMutationId) {
        ProductMutation mutation = mutationRepository.findById(productMutationId)
                .orElseThrow(() -> new RuntimeException("Mutation not found: " + productMutationId));
        validateUserAccess(mutation);
        return productMutationLogRepository.findAllByProductMutationId(productMutationId);
    }

    private void validateUserAccess(ProductMutation mutation) {
        User user = userRepository.findById(Claims.getUserIdFromJwt())
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        if (PermissionUtils.isSuperAdmin(user)) return;
        PermissionUtils.isAdminOfCurrentWarehouse(user, mutation.getOriginId(), mutation.getDestinationId());
    }
}
