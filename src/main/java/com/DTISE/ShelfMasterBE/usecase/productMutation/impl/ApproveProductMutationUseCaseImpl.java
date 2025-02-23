package com.DTISE.ShelfMasterBE.usecase.productMutation.impl;

import com.DTISE.ShelfMasterBE.common.enums.MutationStatusEnum;
import com.DTISE.ShelfMasterBE.common.exceptions.MutationStatusNotFoundException;
import com.DTISE.ShelfMasterBE.common.tools.PermissionUtils;
import com.DTISE.ShelfMasterBE.entity.*;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.*;
import com.DTISE.ShelfMasterBE.usecase.productMutation.ApproveProductMutationUseCase;
import jakarta.persistence.OptimisticLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class ApproveProductMutationUseCaseImpl implements ApproveProductMutationUseCase {
    private final MutationStatusRepository mutationStatusRepo;
    private final ProductMutationRepository mutationRepo;
    private final ProductMutationLogRepository mutationLogRepo;
    private final ProductStockRepository productStockRepo;
    private final UserRepository userRepo;

    public ApproveProductMutationUseCaseImpl(
            MutationStatusRepository mutationStatusRepository,
            ProductMutationRepository productMutationRepository,
            ProductMutationLogRepository productMutationLogRepository,
            ProductStockRepository productStockRepository,
            UserRepository userRepository
    ) {
        mutationStatusRepo = mutationStatusRepository;
        mutationRepo = productMutationRepository;
        mutationLogRepo = productMutationLogRepository;
        productStockRepo = productStockRepository;
        userRepo = userRepository;
    }

    @Override
    @Transactional
    public Long approveProductMutation(Long productMutationId) {
        ProductMutation mutation = mutationRepo.findById(productMutationId)
                .orElseThrow(() -> new RuntimeException("Mutation not found: " + productMutationId));
        if (mutation.getProcessedByUser() != null) throw new RuntimeException("Already processed");
        User user = userRepo.findById(Claims.getUserIdFromJwt())
                .orElseThrow(() -> new RuntimeException("User not found"));
        validateUserAccess(user, mutation.getOriginId());
        processMutation(user, mutation);
        createMutationLog(mutation);
        updateStock(mutation);
        return mutation.getId();
    }

    private void processMutation(User user, ProductMutation mutationToProcess) {
        if(mutationRepo.approveProductMutationWithOptimisticLocking(
                mutationToProcess.getId(),
                user.getId(),
                mutationToProcess.getUpdatedAt(),
                OffsetDateTime.now()
        ) == 0) {
            throw new RuntimeException("Failed to "
                    + "approve because it was modified by another transaction.");
        }
    }

    private void validateUserAccess(User user, Long warehouseId) {
        if (PermissionUtils.isSuperAdmin(user)) return;
        PermissionUtils.isAdminOfCurrentWarehouse(user, warehouseId);
    }

    private void createMutationLog(ProductMutation mutation) {
        MutationStatus status = mutationStatusRepo
                .findFirstByName(MutationStatusEnum.APPROVED)
                .orElseThrow(() -> new MutationStatusNotFoundException("Status not found."));

        ProductMutationLog log = new ProductMutationLog();
        log.setProductMutationId(mutation.getId());
        log.setMutationStatus(status);
        mutationLogRepo.save(log);
    }

    private void updateStock(ProductMutation productMutation) {
        retryWithOptimisticLocking(() -> {
            ProductStock stock = productStockRepo.findFirstByProductIdAndWarehouseId(
                            productMutation.getProduct().getId(), productMutation.getOriginId())
                    .orElseThrow(() -> new RuntimeException("Insufficient stock"));
            if (stock.getQuantity() < productMutation.getQuantity()) throw new RuntimeException("Insufficient stock");
            stock.setQuantity(stock.getQuantity() - productMutation.getQuantity());
            productStockRepo.save(stock);
        });
        updateDestinationStock(productMutation);
    }

    private void updateDestinationStock(ProductMutation productMutation) {
        retryWithOptimisticLocking(() -> {
            ProductStock destinationStock = productStockRepo.findFirstByProductIdAndWarehouseId(
                            productMutation.getProduct().getId(), productMutation.getDestinationId())
                    .orElseGet(() -> createNewStock(productMutation));
            destinationStock.setQuantity(destinationStock.getQuantity() + productMutation.getQuantity());
            productStockRepo.save(destinationStock);
        });
    }

    private ProductStock createNewStock(ProductMutation productMutation) {
        ProductStock newStock = new ProductStock();
        newStock.setProductId(productMutation.getProduct().getId());
        newStock.setWarehouseId(productMutation.getDestinationId());
        newStock.setQuantity(0L);
        newStock.setVersion(0L);
        return newStock;
    }

    private void retryWithOptimisticLocking(RetryingTask task) {
        int attempts = 0, max = 3;
        while (attempts < max) {
            try {
                task.execute();
                attempts = max;
            } catch (OptimisticLockException e) {
                attempts++;
                if (attempts == max) throw new RuntimeException(
                        "Failed to add stock because it was modified by another "
                                + "transaction after 3 retries."
                );
            }
        }
    }

    @FunctionalInterface
    private interface RetryingTask {
        void execute();
    }
}
