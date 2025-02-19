package com.DTISE.ShelfMasterBE.usecase.productMutation.impl;

import com.DTISE.ShelfMasterBE.common.enums.MutationStatusEnum;
import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.exceptions.MutationStatusNotFoundException;
import com.DTISE.ShelfMasterBE.common.tools.PermissionUtils;
import com.DTISE.ShelfMasterBE.entity.*;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.MutationStatusRepository;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.ProductMutationLogRepository;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.ProductMutationRepository;
import com.DTISE.ShelfMasterBE.usecase.productMutation.RejectOrCancelProductMutationUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class RejectOrCancelProductMutationUseCaseImpl implements RejectOrCancelProductMutationUseCase {
    private final MutationStatusRepository mutationStatusRepository;
    private final ProductMutationRepository productMutationRepository;
    private final ProductMutationLogRepository productMutationLogRepository;
    private final UserRepository userRepository;

    public RejectOrCancelProductMutationUseCaseImpl(
            MutationStatusRepository mutationStatusRepository,
            ProductMutationRepository productMutationRepository,
            ProductMutationLogRepository productMutationLogRepository,
            UserRepository userRepository
    ) {
        this.mutationStatusRepository = mutationStatusRepository;
        this.productMutationRepository = productMutationRepository;
        this.productMutationLogRepository = productMutationLogRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Long cancelProductMutation(Long productMutationId) {
        Optional<ProductMutation> existingMutation = productMutationRepository.findById(productMutationId);
        if(existingMutation.isEmpty()) throw new RuntimeException("No product mutation with ID: " + productMutationId);
        ProductMutation mutationToCancel = existingMutation.get();

        User user = userRepository.findById(Claims.getUserIdFromJwt())
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        validateUserAccess(user, mutationToCancel.getDestinationId());

        process(user, mutationToCancel, "cancel");

        createMutationLog(mutationToCancel, MutationStatusEnum.CANCELED);

        return mutationToCancel.getId();
    }

    @Override
    @Transactional
    public Long rejectProductMutation(Long productMutationId) {
        Optional<ProductMutation> existingMutation = productMutationRepository.findById(productMutationId);
        if(existingMutation.isEmpty()) throw new RuntimeException("No product mutation with ID: " + productMutationId);
        ProductMutation mutationToReject = existingMutation.get();

        User user = userRepository.findById(Claims.getUserIdFromJwt())
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        validateUserAccess(user, mutationToReject.getDestinationId());

        process(user, mutationToReject, "reject");

        createMutationLog(mutationToReject, MutationStatusEnum.REJECTED);

        return mutationToReject.getId();
    }

    private void process(User user, ProductMutation mutationToProcess, String actionType) {
        if(productMutationRepository.cancelOrRejectProductMutationWithOptimisticLocking(
                mutationToProcess.getId(),
                user.getId(),
                mutationToProcess.getUpdatedAt(),
                OffsetDateTime.now()
        ) == 0) {
            throw new RuntimeException("Failed to "
                    + actionType + " because it was modified by another transaction.");
        }
    }

    private void validateUserAccess(User user, Long warehouseId) {
        if (PermissionUtils.isSuperAdmin(user)) return;
        PermissionUtils.isAdminOfCurrentWarehouse(user, warehouseId);
    }

    private void createMutationLog(ProductMutation mutation, MutationStatusEnum statusEnum) {
        MutationStatus status = mutationStatusRepository
                .findFirstByName(statusEnum)
                .orElseThrow(() -> new MutationStatusNotFoundException("Status not found."));

        ProductMutationLog log = new ProductMutationLog();
        log.setProductMutationId(mutation.getId());
        log.setMutationStatus(status);
        productMutationLogRepository.save(log);
    }
}
