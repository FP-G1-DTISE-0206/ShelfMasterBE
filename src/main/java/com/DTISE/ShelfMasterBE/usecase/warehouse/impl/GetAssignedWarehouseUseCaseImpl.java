package com.DTISE.ShelfMasterBE.usecase.warehouse.impl;

import com.DTISE.ShelfMasterBE.common.tools.PermissionUtils;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.AssignedWarehouseResponse;
import com.DTISE.ShelfMasterBE.usecase.warehouse.GetAssignedWarehouseUseCase;
import com.DTISE.ShelfMasterBE.usecase.warehouse.GetWarehousesUsecase;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAssignedWarehouseUseCaseImpl implements GetAssignedWarehouseUseCase {
    private final UserRepository userRepo;
    private final GetWarehousesUsecase getWhUseCase;

    public GetAssignedWarehouseUseCaseImpl(
            UserRepository userRepository,
            GetWarehousesUsecase getWarehousesUsecase
    ) {
        userRepo = userRepository;
        getWhUseCase = getWarehousesUsecase;
    }

    @Override
    public List<AssignedWarehouseResponse> getAllAssignedWarehouse(Pageable pageable, String search) {
        User user = userRepo.findById(Claims.getUserIdFromJwt())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (PermissionUtils.isSuperAdmin(user)) {
            return getWhUseCase.getWarehouses(pageable, search)
                    .map(w -> new AssignedWarehouseResponse(w.getId(), w.getName()))
                    .toList();
        }
        return getWarehouses(user, search);
    }

    private List<AssignedWarehouseResponse> getWarehouses(User user, String search) {
        return PermissionUtils.getAssignedWarehouse(user, search);
    }
}
