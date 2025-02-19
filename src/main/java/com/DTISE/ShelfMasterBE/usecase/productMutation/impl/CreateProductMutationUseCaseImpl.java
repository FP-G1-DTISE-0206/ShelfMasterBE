package com.DTISE.ShelfMasterBE.usecase.productMutation.impl;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import com.DTISE.ShelfMasterBE.common.enums.MutationStatusEnum;
import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.exceptions.MutationStatusNotFoundException;
import com.DTISE.ShelfMasterBE.common.exceptions.MutationTypeNotFoundException;
import com.DTISE.ShelfMasterBE.common.tools.PermissionUtils;
import com.DTISE.ShelfMasterBE.entity.*;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.InternalProductMutationRequest;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.*;
import com.DTISE.ShelfMasterBE.usecase.productMutation.CreateProductMutationUseCase;
import org.springframework.transaction.annotation.Transactional;

public class CreateProductMutationUseCaseImpl implements CreateProductMutationUseCase {
    private final MutationTypeRepository mutationTypeRepository;
    private final MutationStatusRepository mutationStatusRepository;
    private final ProductMutationRepository productMutationRepository;
    private final ProductMutationLogRepository productMutationLogRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CreateProductMutationUseCaseImpl(
            MutationTypeRepository mutationTypeRepository,
            MutationStatusRepository mutationStatusRepository,
            ProductMutationRepository productMutationRepository,
            ProductMutationLogRepository productMutationLogRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.mutationTypeRepository = mutationTypeRepository;
        this.mutationStatusRepository = mutationStatusRepository;
        this.productMutationRepository = productMutationRepository;
        this.productMutationLogRepository = productMutationLogRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Long createInternalProductMutation(InternalProductMutationRequest req) {
        User user = userRepository.findById(Claims.getUserIdFromJwt())
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        validateUserAccess(user, req.getWarehouseOriginId());

        ProductMutation newProductMutation = createMutation(user, req);
        productMutationRepository.save(newProductMutation);

        createMutationLog(newProductMutation);

        return newProductMutation.getId();
    }

    private void validateUserAccess(User user, Long warehouseId) {
        if (PermissionUtils.isSuperAdmin(user)) return;
        PermissionUtils.isAdminOfCurrentWarehouse(user, warehouseId);
    }

    private ProductMutation createMutation(User user, InternalProductMutationRequest req) {
        MutationType type = mutationTypeRepository
                .findFirstByOriginTypeAndDestinationType(MutationEntityType.WAREHOUSE, MutationEntityType.WAREHOUSE)
                .orElseThrow(() -> new MutationTypeNotFoundException("Mutation type missing."));

        ProductMutation mutation = req.toEntity();
        mutation.setProduct(getProductById(req.getProductId()));
        mutation.setMutationType(type);
        mutation.setRequestedByUser(user);
        return mutation;
    }

    private Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No product with ID: " + id));
    }

    private void createMutationLog(ProductMutation mutation) {
        MutationStatus status = mutationStatusRepository
                .findFirstByName(MutationStatusEnum.PENDING)
                .orElseThrow(() -> new MutationStatusNotFoundException("Status not found."));

        ProductMutationLog log = new ProductMutationLog();
        log.setProductMutationId(mutation.getId());
        log.setMutationStatus(status);
        productMutationLogRepository.save(log);
    }
}
