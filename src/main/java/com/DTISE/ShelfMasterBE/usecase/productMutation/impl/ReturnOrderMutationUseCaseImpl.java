package com.DTISE.ShelfMasterBE.usecase.productMutation.impl;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import com.DTISE.ShelfMasterBE.common.enums.MutationStatusEnum;
import com.DTISE.ShelfMasterBE.common.exceptions.MutationStatusNotFoundException;
import com.DTISE.ShelfMasterBE.common.exceptions.MutationTypeNotFoundException;
import com.DTISE.ShelfMasterBE.entity.*;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.*;
import com.DTISE.ShelfMasterBE.usecase.productMutation.ReturnOrderMutationUseCase;
import jakarta.persistence.OptimisticLockException;

import java.time.OffsetDateTime;
import java.util.concurrent.atomic.AtomicReference;

public class ReturnOrderMutationUseCaseImpl implements ReturnOrderMutationUseCase {
    private final ProductMutationOrderRepository mutationOrderRepo;
    private final ProductMutationRepository mutationRepo;
    private final ProductStockRepository stockRepo;
    private final UserRepository userRepo;
    private final MutationTypeRepository mutationTypeRepo;
    private final MutationStatusRepository mutationStatusRepo;
    private final ProductMutationLogRepository mutationLogRepo;

    private final User system;
    private final MutationType internalMutationType;
    private final MutationType orderMutationType;
    private final MutationStatus isApproved;

    public ReturnOrderMutationUseCaseImpl(
            ProductMutationOrderRepository productMutationOrderRepository,
            ProductMutationRepository productMutationRepository,
            ProductStockRepository productStockRepository,
            UserRepository userRepository,
            MutationTypeRepository mutationTypeRepository,
            MutationStatusRepository mutationStatusRepository,
            ProductMutationLogRepository productMutationLogRepository) {
        mutationOrderRepo = productMutationOrderRepository;
        mutationRepo = productMutationRepository;
        stockRepo = productStockRepository;
        userRepo = userRepository;
        mutationTypeRepo = mutationTypeRepository;
        mutationStatusRepo = mutationStatusRepository;
        mutationLogRepo = productMutationLogRepository;

        system = getSystem();
        internalMutationType = getType(MutationEntityType.WAREHOUSE);
        orderMutationType = getType(MutationEntityType.USER);
        isApproved = getIsApproved();
    }

    @Override
    public Long returnOrderMutateAll(Long orderId) {
        mutationOrderRepo.findAllByOrderIdOrderByIdDesc(orderId).forEach(this::returnOrderMutate);
        return orderId;
    }

    private void returnOrderMutate(ProductMutationOrder mutationOrder) {
        ProductMutation orderedMutation = mutationRepo.findById(mutationOrder.getOrderedProductMutationId())
                .orElseThrow(() -> new RuntimeException("Fail to auto return mutate: Product Mutation with ID "
                    + mutationOrder.getOrderedProductMutationId() + " not found."));

        if(orderedMutation.getMutationType().equals(internalMutationType)) {
            returnInternalMutation(mutationOrder, orderedMutation);
        } else {
            returnOrder(mutationOrder, orderedMutation);
        }
    }

    private void returnInternalMutation(ProductMutationOrder mutationOrder, ProductMutation orderedMutation) {
        orderedMutation.setQuantity(returnStockToDestination(orderedMutation));
        if(orderedMutation.getQuantity() == 0L) return;
        returnStockToOrigin(orderedMutation);
        ProductMutation returnedMutation = createReturnedProductMutation(
                orderedMutation,
                orderedMutation.getQuantity(),
                internalMutationType);
        createMutationLog(returnedMutation);
        updateMutationOrder(mutationOrder, returnedMutation);
    }

    public void returnOrder(ProductMutationOrder mutationOrder, ProductMutation orderedMutation) {
        returnStockToOrigin(orderedMutation);
        ProductMutation returnedMutation = createReturnedProductMutation(
                orderedMutation,
                orderedMutation.getQuantity(),
                orderMutationType);
        createMutationLog(returnedMutation);
        updateMutationOrder(mutationOrder, returnedMutation);
    }

    private Long returnStockToDestination(ProductMutation orderedMutation) {
        AtomicReference<Long> transferredQty = new AtomicReference<>(0L);
        retryWithOptimisticLocking(() -> {
            ProductStock stock = stockRepo.findFirstByProductIdAndWarehouseId(
                            orderedMutation.getProduct().getId(), orderedMutation.getDestinationId())
                    .orElseThrow(() -> new RuntimeException("Warehouse stock exist but return empty"));

            if (stock.getQuantity() == 0L) {
                transferredQty.set(0L);
                return;
            }
            Long availableQty = Math.min(stock.getQuantity(), orderedMutation.getQuantity());

            stock.setQuantity(stock.getQuantity() - orderedMutation.getQuantity());
            stockRepo.save(stock);

            transferredQty.set(availableQty);
        });
        return transferredQty.get();
    }

    private void returnStockToOrigin(ProductMutation orderedMutation) {
        retryWithOptimisticLocking(() -> {
            ProductStock stock = stockRepo.findFirstByProductIdAndWarehouseId(
                            orderedMutation.getProduct().getId(), orderedMutation.getOriginId())
                    .orElseThrow(() -> new RuntimeException("Warehouse stock exist but return empty"));
            stock.setQuantity(stock.getQuantity() + orderedMutation.getQuantity());
            stockRepo.save(stock);
        });
    }

    private ProductMutation createReturnedProductMutation(
            ProductMutation orderedMutation,
            Long quantity,
            MutationType type
    ) {
        ProductMutation mutation = new ProductMutation();
        mutation.setProduct(orderedMutation.getProduct());
        mutation.setMutationType(type);
        mutation.setOriginId(orderedMutation.getDestinationId());
        mutation.setDestinationId(orderedMutation.getOriginId());
        mutation.setQuantity(quantity);
        mutation.setRequestedByUser(system);
        mutation.setProcessedByUser(system);
        mutation.setIsApproved(true);
        return mutationRepo.save(mutation);
    }

    private void createMutationLog(ProductMutation mutation) {
        ProductMutationLog log = new ProductMutationLog();
        log.setProductMutationId(mutation.getId());
        log.setMutationStatus(isApproved);
        mutationLogRepo.save(log);
    }

    private void updateMutationOrder(ProductMutationOrder mutationOrder, ProductMutation returnedMutation) {
        if(mutationOrder.getReturnedProductMutationId() != null) throw new RuntimeException("Fail to auto return mutate: "
            + "already returned");
        mutationOrder.setReturnedProductMutationId(returnedMutation.getId());
        mutationOrder.setUpdatedAt(OffsetDateTime.now());
        mutationOrderRepo.save(mutationOrder);
    }

    private User getSystem() {
        return userRepo.findByEmail("system@localhost")
                .orElseThrow(() -> new RuntimeException("Fail to auto return mutate: system not found."));
    }

    private MutationType getType(MutationEntityType origin) {
        return mutationTypeRepo.findFirstByOriginTypeAndDestinationType(origin, MutationEntityType.WAREHOUSE)
                .orElseThrow(() -> new MutationTypeNotFoundException("Mutation type missing."));
    }

    private MutationStatus getIsApproved() {
        return mutationStatusRepo
                .findFirstByName(MutationStatusEnum.APPROVED)
                .orElseThrow(() -> new MutationStatusNotFoundException("Status not found."));
    }

    private void retryWithOptimisticLocking(RetryingTask task) {
        int attempts = 0, max = 5;
        while (attempts < max) {
            try {
                task.execute();
                attempts = max;
            } catch (OptimisticLockException e) {
                attempts++;
                if (attempts == max) throw new RuntimeException(
                        "Failed to auto mutate stock because it was modified by another "
                                + "transaction after 5 retries."
                );
            }
        }
    }

    @FunctionalInterface
    private interface RetryingTask {
        void execute();
    }
}
