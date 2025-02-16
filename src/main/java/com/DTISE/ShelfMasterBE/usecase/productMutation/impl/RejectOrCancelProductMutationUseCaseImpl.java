package com.DTISE.ShelfMasterBE.usecase.productMutation.impl;

import com.DTISE.ShelfMasterBE.common.enums.MutationStatusEnum;
import com.DTISE.ShelfMasterBE.common.exceptions.MutationStatusNotFoundException;
import com.DTISE.ShelfMasterBE.entity.*;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.MutationStatusRepository;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.ProductMutationLogRepository;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.ProductMutationRepository;
import com.DTISE.ShelfMasterBE.usecase.productMutation.RejectOrCancelProductMutationUseCase;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class RejectOrCancelProductMutationUseCaseImpl implements RejectOrCancelProductMutationUseCase {
    private final MutationStatusRepository mutationStatusRepository;
    private final ProductMutationRepository productMutationRepository;
    private final ProductMutationLogRepository productMutationLogRepository;

    public RejectOrCancelProductMutationUseCaseImpl(
            MutationStatusRepository mutationStatusRepository,
            ProductMutationRepository productMutationRepository,
            ProductMutationLogRepository productMutationLogRepository
    ) {
        this.mutationStatusRepository = mutationStatusRepository;
        this.productMutationRepository = productMutationRepository;
        this.productMutationLogRepository = productMutationLogRepository;
    }

    @Override
    @Transactional
    public Long cancelProductMutation(User user, Long productMutationId) {
        Optional<ProductMutation> existingMutation = productMutationRepository.findById(productMutationId);
        if(existingMutation.isEmpty()) throw new RuntimeException("No product mutation with ID: " + productMutationId);
        ProductMutation mutationToCancel = existingMutation.get();
        validateUserAccess(user, mutationToCancel.getDestinationId());

        process(user, mutationToCancel, "cancel");

        createMutationLog(mutationToCancel, MutationStatusEnum.CANCELED);

        return mutationToCancel.getId();
    }

    @Override
    @Transactional
    public Long rejectProductMutation(User user, Long productMutationId) {
        Optional<ProductMutation> existingMutation = productMutationRepository.findById(productMutationId);
        if(existingMutation.isEmpty()) throw new RuntimeException("No product mutation with ID: " + productMutationId);
        ProductMutation mutationToReject = existingMutation.get();
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
        if (user.getWarehouses().isEmpty()) return;
        boolean isAdmin = user.getWarehouses().stream()
                .anyMatch(w -> Objects.equals(w.getId(), warehouseId));
        if (!isAdmin) {
            throw new AuthorizationDeniedException("Unauthorized: Not an admin of current warehouse.");
        }
    }

    private void createMutationLog(ProductMutation mutation, MutationStatusEnum statusEnum) {
        MutationStatus status = mutationStatusRepository
                .findFirstByName(statusEnum)
                .orElseThrow(() -> new MutationStatusNotFoundException("Status not found."));

        ProductMutationLog log = new ProductMutationLog();
        log.setProductMutationId(mutation.getId());
        log.setMutationStatusId(status.getId());
        productMutationLogRepository.save(log);
    }
}
