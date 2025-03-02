package com.DTISE.ShelfMasterBE.usecase.report.impl;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.tools.DateConverter;
import com.DTISE.ShelfMasterBE.common.tools.PermissionUtils;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.report.dto.SalesReportRequest;
import com.DTISE.ShelfMasterBE.infrastructure.report.dto.SalesReportResponse;
import com.DTISE.ShelfMasterBE.infrastructure.report.repository.ProductMutationOrderRepository1;
import com.DTISE.ShelfMasterBE.usecase.report.GetSalesReportUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GetSalesReportUseCaseImpl implements GetSalesReportUseCase {
    private final UserRepository userRepo;
    private final ProductMutationOrderRepository1 mutationOrderRepo;

    public GetSalesReportUseCaseImpl(
            UserRepository userRepository,
            ProductMutationOrderRepository1 productMutationOrderRepository1
    ) {
        userRepo = userRepository;
        mutationOrderRepo = productMutationOrderRepository1;
    }

    @Override
    public List<SalesReportResponse> getSalesReport(SalesReportRequest req) {
        validateUserAccess(req.getWarehouseId());
        return mutationOrderRepo.getSalesReport(
                MutationEntityType.WAREHOUSE,
                MutationEntityType.USER,
                req.getProductId(),
                req.getCategoryId(),
                DateConverter.getStartOfDay(req.getStartDate()),
                DateConverter.getEndOfDay(req.getEndDate()),
                req.getWarehouseId()
        );
    }

    @Override
    public Page<SalesReportResponse> getSalesReportPreview(
            Pageable pageable,
            Long productId,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate,
            Long warehouseId) {
        validateUserAccess(warehouseId);
        return mutationOrderRepo.getSalesReportPreview(
                pageable,
                MutationEntityType.WAREHOUSE,
                MutationEntityType.USER,
                productId,
                categoryId,
                DateConverter.getStartOfDay(startDate),
                DateConverter.getEndOfDay(endDate),
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
