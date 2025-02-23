package com.DTISE.ShelfMasterBE.usecase.productMutation.impl;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import com.DTISE.ShelfMasterBE.common.enums.MutationStatusEnum;
import com.DTISE.ShelfMasterBE.common.exceptions.InsufficientStockException;
import com.DTISE.ShelfMasterBE.common.exceptions.MutationStatusNotFoundException;
import com.DTISE.ShelfMasterBE.common.exceptions.MutationTypeNotFoundException;
import com.DTISE.ShelfMasterBE.common.tools.Pagination;
import com.DTISE.ShelfMasterBE.entity.*;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.order.repository.OrderItemRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.AutoMutationRequest;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.*;
import com.DTISE.ShelfMasterBE.usecase.productMutation.OrderMutationUseCase;
import jakarta.persistence.OptimisticLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderMutationUseCaseImpl implements OrderMutationUseCase {
    private final UserRepository userRepo;
    private final OrderItemRepository orderItemRepo;
    private final ProductMutationRepository mutationRepo;
    private final ProductMutationLogRepository mutationLogRepo;
    private final ProductStockRepository stockRepo;
    private final MutationTypeRepository mutationTypeRepo;
    private final ProductRepository productRepo;
    private final MutationStatusRepository mutationStatusRepo;
    private final ProductMutationOrderRepository mutationOrderRepo;

    private User system;
    private MutationType internalMutationType;
    private MutationType orderMutationType;
    private Long orderId;
    private Long buyerId;
    private Long localWarehouseId;
    private MutationStatus isApproved;

    public OrderMutationUseCaseImpl(
            UserRepository userRepository,
            OrderItemRepository orderItemRepository,
            ProductMutationRepository productMutationRepository,
            ProductMutationLogRepository productMutationLogRepository,
            ProductStockRepository productStockRepository,
            MutationTypeRepository mutationTypeRepository,
            ProductRepository productRepository,
            MutationStatusRepository mutationStatusRepository,
            ProductMutationOrderRepository productMutationOrderRepository) {
        userRepo = userRepository;
        orderItemRepo = orderItemRepository;
        mutationRepo = productMutationRepository;
        mutationLogRepo = productMutationLogRepository;
        stockRepo = productStockRepository;
        mutationTypeRepo = mutationTypeRepository;
        productRepo = productRepository;
        mutationStatusRepo = mutationStatusRepository;
        mutationOrderRepo = productMutationOrderRepository;
    }

    @Override
    @Transactional
    public Long orderMutateAll(AutoMutationRequest request) {
        prepareSystem(request.getOrderId(), request.getUserId(), request.getWarehouseId());
        orderItemRepo.findAllByOrderId(request.getOrderId())
                .forEach(this::processMutation);
        return request.getOrderId();
    }

    @Override
    public Long getProductTotalStock(Long productId) {
        return stockRepo.getTotalStockByProductId(productId);
    }

    private void processMutation(OrderItem orderItem) {
        if(stockRepo.getTotalStockByProductId(orderItem.getProductId()) <= 0L) {
            throw new InsufficientStockException("Insufficient stock");
        }

        ProductStock stock = stockRepo.findFirstByProductIdAndWarehouseId(
                        orderItem.getProductId(), localWarehouseId)
                .orElseGet(() -> createNewEmptyStock(orderItem.getProductId()));

        if (stock.getQuantity() < orderItem.getQuantity()) {
            handleInsufficientLocalStock(orderItem);
        } else {
            orderAutoMutate(
                    orderItem.getProductId(),
                    orderItem.getQuantity(),
                    buyerId,
                    localWarehouseId
            );
        }
    }

    private ProductStock createNewEmptyStock(Long productId) {
        ProductStock newStock = new ProductStock();
        newStock.setProductId(productId);
        newStock.setWarehouseId(localWarehouseId);
        newStock.setQuantity(0L);
        newStock.setVersion(0L);
        return stockRepo.save(newStock);
    }

    private void handleInsufficientLocalStock(OrderItem orderItem) {
        Integer length = 10, page = 1;
        AtomicReference<Long> remainingQuantity = new AtomicReference<>(orderItem.getQuantity());
        while (remainingQuantity.get() > 0) {
            Page<ProductStock> productStocks = fetchProductStocks(orderItem.getProductId(), length, page);
            if(productStocks.isEmpty() && remainingQuantity.get() < 0) {
                throw new InsufficientStockException("Insufficient stock");
            }
            processStockTransfers(productStocks, orderItem, remainingQuantity);
            if(!productStocks.hasNext() && remainingQuantity.get() > 0) {
                throw new InsufficientStockException("Insufficient stock");
            }
            page++;
        }

        orderAutoMutate(orderItem.getProductId(), orderItem.getQuantity(),
                buyerId, localWarehouseId);
    }

    private Page<ProductStock> fetchProductStocks(Long productId, Integer length, Integer page) {
        Pageable pageable = Pagination.createPageable((length * page) - length, length);
        return stockRepo.getStockByProductIdOrderByLocation(
                pageable, productId, localWarehouseId
        );
    }

    private void processStockTransfers(Page<ProductStock> productStocks, OrderItem orderItem,
                                       AtomicReference<Long> remainingQuantity) {
        productStocks.forEach(productStock -> {
            if(productStock.getWarehouseId().equals(localWarehouseId)) {
                remainingQuantity.getAndSet(
                        remainingQuantity.get() - productStock.getQuantity()
                );
            } else {
                transferStockBetweenWarehouse(orderItem.getProductId(), productStock, remainingQuantity);
            }
        });
    }

    private void transferStockBetweenWarehouse(Long productId, ProductStock productStock,
                                               AtomicReference<Long> remainingQuantity) {
        Long transferQty = Math.min(productStock.getQuantity(), remainingQuantity.get());
        remainingQuantity.getAndSet(remainingQuantity.get() - internalAutoMutate(productId,
                transferQty, productStock.getWarehouseId(), localWarehouseId));
    }

    private Long internalAutoMutate(
            Long productId,
            Long quantity,
            Long originId,
            Long destinationId
    ) {
        ProductMutation newProductMutation = toProductMutationEntity(
                productId, quantity, originId, destinationId, internalMutationType);
        newProductMutation.setQuantity(updateOriginStock(newProductMutation));
        if(newProductMutation.getQuantity() == 0L) {
            return 0L;
        }

        newProductMutation = mutationRepo.save(newProductMutation);
        createMutationLog(newProductMutation);
        createMutationOrder(newProductMutation);
        updateDestinationStock(newProductMutation);
        return newProductMutation.getQuantity();
    }

    private void orderAutoMutate(
            Long productId,
            Long quantity,
            Long originId,
            Long destinationId
    ) {
        ProductMutation newProductMutation = createAutoMutation(
                productId, quantity, originId, destinationId, orderMutationType);

        createMutationLog(newProductMutation);
        createMutationOrder(newProductMutation);

        updateOrderStock(newProductMutation);
    }

    private ProductMutation createAutoMutation(
            Long productId,
            Long quantity,
            Long originId,
            Long destinationId,
            MutationType type
    ) {
        return mutationRepo.save(toProductMutationEntity(
                productId, quantity, originId, destinationId, type
        ));
    }

    private ProductMutation toProductMutationEntity(
            Long productId,
            Long quantity,
            Long originId,
            Long destinationId,
            MutationType type
    ) {
        ProductMutation mutation = new ProductMutation();
        mutation.setProduct(getProductById(productId));
        mutation.setMutationType(type);
        mutation.setOriginId(originId);
        mutation.setDestinationId(destinationId);
        mutation.setQuantity(quantity);
        mutation.setRequestedByUser(system);
        mutation.setProcessedByUser(system);
        mutation.setIsApproved(true);
        return mutation;
    }

    private void createMutationLog(ProductMutation mutation) {
        ProductMutationLog log = new ProductMutationLog();
        log.setProductMutationId(mutation.getId());
        log.setMutationStatus(isApproved);
        mutationLogRepo.save(log);
    }

    private void createMutationOrder(ProductMutation mutation) {
        ProductMutationOrder newMutationOrder = new ProductMutationOrder();
        newMutationOrder.setOrderId(orderId);
        newMutationOrder.setOrderedProductMutationId(mutation.getId());
        mutationOrderRepo.save(newMutationOrder);
    }

    private Product getProductById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("No product with ID: " + id));
    }

    private void prepareSystem(Long orderId, Long buyerId, Long localWarehouseId) {
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.localWarehouseId = localWarehouseId;
        setSystem();
        internalMutationType = getType(MutationEntityType.WAREHOUSE);
        orderMutationType = getType(MutationEntityType.USER);
        setIsApproved();
    }

    private void setSystem() {
        this.system = userRepo.findByEmail("system@localhost")
                .orElseThrow(() -> new RuntimeException("Fail to auto mutate: system not found."));
    }

    private MutationType getType(MutationEntityType destination) {
        return mutationTypeRepo.findFirstByOriginTypeAndDestinationType(MutationEntityType.WAREHOUSE, destination)
                .orElseThrow(() -> new MutationTypeNotFoundException("Mutation type missing."));
    }

    private void setIsApproved() {
        isApproved = mutationStatusRepo
                .findFirstByName(MutationStatusEnum.APPROVED)
                .orElseThrow(() -> new MutationStatusNotFoundException("Status not found."));
    }

    private Long updateOriginStock(ProductMutation productMutation) {
        AtomicReference<Long> transferredQty = new AtomicReference<>(0L);
        retryWithOptimisticLocking(() -> {
            ProductStock stock = stockRepo.findFirstByProductIdAndWarehouseId(
                            productMutation.getProduct().getId(), productMutation.getOriginId())
                    .orElseThrow(() -> new RuntimeException("Origin warehouse stock exist but return empty"));
            if (stock.getQuantity() == 0L) {
                transferredQty.set(0L);
                return;
            }

            Long availableQty = Math.min(stock.getQuantity(), productMutation.getQuantity());
            stock.setQuantity(stock.getQuantity() - availableQty);
            stockRepo.save(stock);

            transferredQty.set(availableQty);
        });
        return transferredQty.get();
    }

    private void updateDestinationStock(ProductMutation productMutation) {
        retryWithOptimisticLocking(() -> {
            ProductStock destinationStock = stockRepo.findFirstByProductIdAndWarehouseId(
                            productMutation.getProduct().getId(), productMutation.getDestinationId())
                    .orElseThrow(() -> new RuntimeException("Destination warehouse stock added but return empty"));
            destinationStock.setQuantity(destinationStock.getQuantity() + productMutation.getQuantity());
            stockRepo.save(destinationStock);
        });
    }

    private void updateOrderStock(ProductMutation productMutation) {
        retryWithOptimisticLocking(() -> {
            ProductStock stock = stockRepo.findFirstByProductIdAndWarehouseId(
                            productMutation.getProduct().getId(), productMutation.getOriginId())
                    .orElseThrow(() -> new RuntimeException("Local warehouse stock exist but return empty"));
            if (stock.getQuantity() < productMutation.getQuantity()) throw new InsufficientStockException("Insufficient stock");
            stock.setQuantity(stock.getQuantity() - productMutation.getQuantity());
            stockRepo.save(stock);
        });
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
