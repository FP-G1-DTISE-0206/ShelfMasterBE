package com.DTISE.ShelfMasterBE.usecase.productMutation.impl;

import com.DTISE.ShelfMasterBE.entity.OrderItem;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.order.repository.OrderItemRepository;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.AutoMutationRequest;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.ProductMutationLogRepository;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.ProductMutationRepository;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.ProductStockRepository;
import com.DTISE.ShelfMasterBE.usecase.productMutation.AutoMutationUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutoMutationUseCaseImpl implements AutoMutationUseCase {
    private final UserRepository userRepo;
    private final OrderItemRepository orderItemRepo;
    private final ProductMutationRepository mutationRepo;
    private final ProductMutationLogRepository mutationLogRepo;
    private final ProductStockRepository stockRepo;

    public AutoMutationUseCaseImpl(
            UserRepository userRepository,
            OrderItemRepository orderItemRepository,
            ProductMutationRepository productMutationRepository,
            ProductMutationLogRepository productMutationLogRepository,
            ProductStockRepository productStockRepository) {
        userRepo = userRepository;
        orderItemRepo = orderItemRepository;
        mutationRepo = productMutationRepository;
        mutationLogRepo = productMutationLogRepository;
        stockRepo = productStockRepository;
    }

    @Override
    @Transactional
    public Long autoMutateAll(AutoMutationRequest request) {
        orderItemRepo.findAllByOrderId(request.getOrderId())
                .forEach(this::autoMutate);
        return 0L;
    }

    private void autoMutate(OrderItem orderItem) {
        //mutationRepo.save()
        //mutationLogRepo.save()
        //stockRepo.save()
    }

    private User getSystem() {
        return userRepo.findByEmail("system@localhost")
                .orElseThrow(() -> new RuntimeException("Fail to auto mutate: system not found."));
    }
}
