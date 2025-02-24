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
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.AddProductStockRequest;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.*;
import com.DTISE.ShelfMasterBE.usecase.productMutation.AddProductStockUseCase;
import jakarta.persistence.OptimisticLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class AddProductStockUseCaseImpl implements AddProductStockUseCase {
    private final MutationTypeRepository mutationTypeRepository;
    private final MutationStatusRepository mutationStatusRepository;
    private final ProductMutationRepository productMutationRepository;
    private final ProductMutationLogRepository productMutationLogRepository;
    private final ProductStockRepository productStockRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public AddProductStockUseCaseImpl(
            MutationTypeRepository mutationTypeRepository,
            MutationStatusRepository mutationStatusRepository,
            ProductMutationRepository productMutationRepository,
            ProductMutationLogRepository productMutationLogRepository,
            ProductStockRepository productStockRepository,
            UserRepository userRepository,
            ProductRepository productRepository
    ) {
        this.mutationTypeRepository = mutationTypeRepository;
        this.mutationStatusRepository = mutationStatusRepository;
        this.productMutationRepository = productMutationRepository;
        this.productMutationLogRepository = productMutationLogRepository;
        this.productStockRepository = productStockRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Long addProductStock(AddProductStockRequest req) {
        User user = userRepository.findById(Claims.getUserIdFromJwt())
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        validateUserAccess(user, req.getWarehouseId());

        ProductMutation newProductMutation = createMutation(user, req);
        productMutationRepository.save(newProductMutation);

        createMutationLog(newProductMutation);

        return retryWithOptimisticLocking(() -> updateOrCreateStock(req));
    }

    private void validateUserAccess(User user, Long warehouseId) {
        if (PermissionUtils.isSuperAdmin(user)) return;
        PermissionUtils.isAdminOfCurrentWarehouse(user, warehouseId);
    }

    private ProductMutation createMutation(User user, AddProductStockRequest req) {
        MutationType type = mutationTypeRepository
                .findFirstByOriginTypeAndDestinationType(MutationEntityType.VENDOR, MutationEntityType.WAREHOUSE)
                .orElseThrow(() -> new MutationTypeNotFoundException("Mutation type missing."));

        ProductMutation mutation = req.toEntity();
        mutation.setProduct(getProductById(req.getProductId()));
        mutation.setMutationType(type);
        mutation.setRequestedByUser(user);
        mutation.setProcessedByUser(getSystem());
        mutation.setIsApproved(true);
        return mutation;
    }

    private Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No product with ID: " + id));
    }

    private User getSystem() {
        return userRepository.findByEmail("system@localhost")
                .orElseThrow(() -> new RuntimeException("Fail to auto mutate: system not found."));
    }

    private void createMutationLog(ProductMutation mutation) {
        MutationStatus status = mutationStatusRepository
                .findFirstByName(MutationStatusEnum.APPROVED)
                .orElseThrow(() -> new MutationStatusNotFoundException("Status not found."));

        ProductMutationLog log = new ProductMutationLog();
        log.setProductMutationId(mutation.getId());
        log.setMutationStatus(status);
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
        newStock.setVersion(0L);
        return productStockRepository.save(newStock).getId();
    }

    private Long updateExistingStock(ProductStock stock, Long quantity) {
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