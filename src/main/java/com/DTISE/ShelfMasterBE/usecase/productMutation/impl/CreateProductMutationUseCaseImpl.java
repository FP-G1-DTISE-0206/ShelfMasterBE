package com.DTISE.ShelfMasterBE.usecase.productMutation.impl;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import com.DTISE.ShelfMasterBE.common.enums.MutationStatusEnum;
import com.DTISE.ShelfMasterBE.common.exceptions.MutationStatusNotFoundException;
import com.DTISE.ShelfMasterBE.common.exceptions.MutationTypeNotFoundException;
import com.DTISE.ShelfMasterBE.entity.*;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.InternalProductMutationRequest;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.*;
import com.DTISE.ShelfMasterBE.usecase.productMutation.CreateProductMutationUseCase;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

public class CreateProductMutationUseCaseImpl implements CreateProductMutationUseCase {
    private final MutationTypeRepository mutationTypeRepository;
    private final MutationStatusRepository mutationStatusRepository;
    private final ProductMutationRepository productMutationRepository;
    private final ProductMutationLogRepository productMutationLogRepository;

    public CreateProductMutationUseCaseImpl(
            MutationTypeRepository mutationTypeRepository,
            MutationStatusRepository mutationStatusRepository,
            ProductMutationRepository productMutationRepository,
            ProductMutationLogRepository productMutationLogRepository
    ) {
        this.mutationTypeRepository = mutationTypeRepository;
        this.mutationStatusRepository = mutationStatusRepository;
        this.productMutationRepository = productMutationRepository;
        this.productMutationLogRepository = productMutationLogRepository;
    }

    @Override
    @Transactional
    public Long createInternalProductMutation(User user, InternalProductMutationRequest req) {
        validateUserAccess(user, req.getWarehouseOriginId());

        ProductMutation newProductMutation = createMutation(user, req);
        productMutationRepository.save(newProductMutation);

        createMutationLog(newProductMutation);

        return newProductMutation.getId();
    }

    private void validateUserAccess(User user, Long warehouseId) {
        if (user.getWarehouses().isEmpty()) return;
        boolean isAdmin = user.getWarehouses().stream()
                .anyMatch(w -> Objects.equals(w.getId(), warehouseId));
        if (!isAdmin) {
            throw new AuthorizationDeniedException("Unauthorized: Not an admin of current warehouse.");
        }
    }

    private ProductMutation createMutation(User user, InternalProductMutationRequest req) {
        MutationType type = mutationTypeRepository
                .findFirstByOriginTypeAndDestinationType(MutationEntityType.WAREHOUSE, MutationEntityType.WAREHOUSE)
                .orElseThrow(() -> new MutationTypeNotFoundException("Mutation type missing."));

        ProductMutation mutation = req.toEntity();
        mutation.setMutationTypeId(type.getId());
        mutation.setRequestedBy(user.getId());
        return mutation;
    }

    private void createMutationLog(ProductMutation mutation) {
        MutationStatus status = mutationStatusRepository
                .findFirstByName(MutationStatusEnum.PENDING)
                .orElseThrow(() -> new MutationStatusNotFoundException("Status not found."));

        ProductMutationLog log = new ProductMutationLog();
        log.setProductMutationId(mutation.getId());
        log.setMutationStatusId(status.getId());
        productMutationLogRepository.save(log);
    }
}
