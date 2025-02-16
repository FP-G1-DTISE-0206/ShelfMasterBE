package com.DTISE.ShelfMasterBE.usecase.productMutation.impl;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import com.DTISE.ShelfMasterBE.common.enums.MutationStatusEnum;
import com.DTISE.ShelfMasterBE.common.exceptions.MutationStatusNotFoundException;
import com.DTISE.ShelfMasterBE.common.exceptions.MutationTypeNotFoundException;
import com.DTISE.ShelfMasterBE.entity.*;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.AddProductStockRequest;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.*;
import com.DTISE.ShelfMasterBE.usecase.productMutation.AddProductStockUseCase;
import jakarta.persistence.OptimisticLockException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class AddProductStockUseCaseImpl implements AddProductStockUseCase {
    private final MutationTypeRepository mutationTypeRepository;
    private final MutationStatusRepository mutationStatusRepository;
    private final ProductMutationRepository productMutationRepository;
    private final ProductMutationLogRepository productMutationLogRepository;
    private final ProductStockRepository productStockRepository;

    public AddProductStockUseCaseImpl(
            MutationTypeRepository mutationTypeRepository,
            MutationStatusRepository mutationStatusRepository,
            ProductMutationRepository productMutationRepository,
            ProductMutationLogRepository productMutationLogRepository,
            ProductStockRepository productStockRepository
    ) {
        this.mutationTypeRepository = mutationTypeRepository;
        this.mutationStatusRepository = mutationStatusRepository;
        this.productMutationRepository = productMutationRepository;
        this.productMutationLogRepository = productMutationLogRepository;
        this.productStockRepository = productStockRepository;
    }

    @Override
    @Transactional
    public Long addProductStock(User user, AddProductStockRequest req) {
        validateUserAccess(user, req.getWarehouseId());

        ProductMutation newProductMutation = createMutation(user, req);
        productMutationRepository.save(newProductMutation);

        createMutationLog(newProductMutation);

        return retryWithOptimisticLocking(() -> updateOrCreateStock(req));
    }

    private void validateUserAccess(User user, Long warehouseId) {
        if (user.getWarehouses().isEmpty()) return;
        boolean isAdmin = user.getWarehouses().stream()
                .anyMatch(w -> Objects.equals(w.getId(), warehouseId));
        if (!isAdmin) {
            throw new AuthorizationDeniedException("Unauthorized: Not an admin.");
        }
    }

    private ProductMutation createMutation(User user, AddProductStockRequest req) {
        MutationType type = mutationTypeRepository
                .findFirstByOriginTypeAndDestinationType(MutationEntityType.VENDOR, MutationEntityType.WAREHOUSE)
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

    private Long updateOrCreateStock(AddProductStockRequest req) {
        Optional<ProductStock> productStock = productStockRepository
                .findFirstByProductIdAndWarehouseId(req.getProductId(), req.getWarehouseId());

        if (productStock.isEmpty()) {
            return createNewStock(req);
        } else {
            return updateExistingStock(productStock.get(), req.getQuantity());
        }
    }

    private Long createNewStock(AddProductStockRequest req) {
        ProductStock newStock = new ProductStock();
        newStock.setProductId(req.getProductId());
        newStock.setWarehouseId(req.getWarehouseId());
        newStock.setQuantity(req.getQuantity());
        return productStockRepository.save(newStock).getId();
    }

    private Long updateExistingStock(ProductStock stock, int quantity) {
        stock.setQuantity(stock.getQuantity() + quantity);
        stock.setUpdatedAt(OffsetDateTime.now());
        return productStockRepository.save(stock).getId();
    }

    private Long retryWithOptimisticLocking(RetryingTask task) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                return task.execute();
            } catch (OptimisticLockException e) {
                attempts++;
                if (attempts == 3) throw new RuntimeException(
                        "Failed to add stock because it was modified by another "
                        + "transaction after 3 retries."
                );
            }
        }
        return null;
    }

    @FunctionalInterface
    private interface RetryingTask {
        Long execute();
    }
}