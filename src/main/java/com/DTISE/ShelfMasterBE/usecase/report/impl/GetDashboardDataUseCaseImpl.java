package com.DTISE.ShelfMasterBE.usecase.report.impl;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.tools.DateConverter;
import com.DTISE.ShelfMasterBE.common.tools.Pagination;
import com.DTISE.ShelfMasterBE.common.tools.PermissionUtils;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.report.dto.GraphResponse;
import com.DTISE.ShelfMasterBE.infrastructure.report.dto.PopularProductResponse;
import com.DTISE.ShelfMasterBE.infrastructure.report.dto.SalesInfoCardResponse;
import com.DTISE.ShelfMasterBE.infrastructure.report.repository.ProductMutationOrderRepository1;
import com.DTISE.ShelfMasterBE.usecase.report.GetDashboardDataUseCase;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GetDashboardDataUseCaseImpl implements GetDashboardDataUseCase {
    private final UserRepository userRepo;
    private final ProductMutationOrderRepository1 mutationOrderRepo;

    public GetDashboardDataUseCaseImpl(
            UserRepository userRepository,
            ProductMutationOrderRepository1 productMutationOrderRepository1
    ) {
        userRepo = userRepository;
        mutationOrderRepo = productMutationOrderRepository1;
    }

    @Override
    public List<SalesInfoCardResponse> getSalesInfo(LocalDate date, Long warehouseId) {
        validateUserAccess(warehouseId);
        return List.of(
                getTotalSalesThisWeek(date, warehouseId),
                getTotalSalesThisMonth(date, warehouseId),
                getTotalSalesThisYear(date, warehouseId)
        );
    }

    @Override
    public List<PopularProductResponse> getThisMonthPopularProducts(LocalDate date, Long warehouseId) {
        validateUserAccess(warehouseId);
        return mutationOrderRepo.getThisMonthPopularProducts(
                PageRequest.of(0,3),
                MutationEntityType.WAREHOUSE,
                MutationEntityType.USER,
                DateConverter.getStartOfMonth(date, 0),
                warehouseId
        );
    }

    @Override
    public List<GraphResponse> getSalesGraphThisMonth(LocalDate date, Long warehouseId) {
        validateUserAccess(warehouseId);
        return mutationOrderRepo.getSalesGraphThisMonth(
                MutationEntityType.WAREHOUSE,
                MutationEntityType.USER,
                DateConverter.getStartOfMonth(date, 0),
                warehouseId
        );
    }

    @Override
    public List<GraphResponse> getSalesGraphThisYear(LocalDate date, Long warehouseId) {
        validateUserAccess(warehouseId);
        return mutationOrderRepo.getSalesGraphThisYear(
                MutationEntityType.WAREHOUSE,
                MutationEntityType.USER,
                date.getYear(),
                warehouseId
        );
    }

    private SalesInfoCardResponse getTotalSalesThisWeek(LocalDate date, Long warehouseId) {
        return mutationOrderRepo.getTotalSalesThisWeek(
                MutationEntityType.WAREHOUSE,
                MutationEntityType.USER,
                DateConverter.getStartOfWeek(date, 0),
                DateConverter.getStartOfWeek(date, 1),
                warehouseId
        );
    }

    private SalesInfoCardResponse getTotalSalesThisMonth(LocalDate date, Long warehouseId) {
        return mutationOrderRepo.getTotalSalesThisMonth(
                MutationEntityType.WAREHOUSE,
                MutationEntityType.USER,
                DateConverter.getStartOfMonth(date, 0),
                DateConverter.getStartOfMonth(date, 1),
                warehouseId
        );
    }

    private SalesInfoCardResponse getTotalSalesThisYear(LocalDate date, Long warehouseId) {
        return mutationOrderRepo.getTotalSalesThisYear(
                MutationEntityType.WAREHOUSE,
                MutationEntityType.USER,
                date.getYear(),
                warehouseId
        );
    }

    private void validateUserAccess(Long warehouseId) {
        User user = userRepo.findById(Claims.getUserIdFromJwt())
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        if (PermissionUtils.isSuperAdmin(user)) return;
        PermissionUtils.isAdminOfCurrentWarehouse(user, warehouseId);
    }
}
