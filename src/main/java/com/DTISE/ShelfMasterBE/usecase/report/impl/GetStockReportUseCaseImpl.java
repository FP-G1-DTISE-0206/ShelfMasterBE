package com.DTISE.ShelfMasterBE.usecase.report.impl;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.tools.DateConverter;
import com.DTISE.ShelfMasterBE.common.tools.PermissionUtils;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.report.dto.StockReportRequest;
import com.DTISE.ShelfMasterBE.infrastructure.report.dto.StockReportResponse;
import com.DTISE.ShelfMasterBE.infrastructure.report.repository.ProductMutationRepository1;
import com.DTISE.ShelfMasterBE.usecase.report.GetStockReportUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GetStockReportUseCaseImpl implements GetStockReportUseCase {
    private final UserRepository userRepo;
    private final ProductMutationRepository1 mutationRepo;

    public GetStockReportUseCaseImpl(
            UserRepository userRepository,
            ProductMutationRepository1 productMutationRepository1
    ) {
        userRepo = userRepository;
        mutationRepo = productMutationRepository1;
    }

    @Override
    public List<StockReportResponse> getStockReport(StockReportRequest req) {
        validateUserAccess(req.getWarehouseId());
        return mutationRepo.getSalesReport(
                MutationEntityType.USER,
                MutationEntityType.VENDOR,
                MutationEntityType.WAREHOUSE,
                req.getProductId(),
                DateConverter.getStartOfDay(req.getStartDate()),
                DateConverter.getEndOfDay(req.getEndDate()),
                req.getWarehouseId()
        );
    }

    @Override
    public Page<StockReportResponse> getStockReportPreview(
            Pageable pageable,
            Long productId,
            LocalDate startDate,
            LocalDate endDate,
            Long warehouseId
    ) {
        validateUserAccess(warehouseId);
        return mutationRepo.getSalesReportPreview(
                pageable,
                MutationEntityType.USER,
                MutationEntityType.VENDOR,
                MutationEntityType.WAREHOUSE,
                productId,
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
