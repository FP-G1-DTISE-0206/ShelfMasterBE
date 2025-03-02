package com.DTISE.ShelfMasterBE.usecase.report;

import com.DTISE.ShelfMasterBE.infrastructure.report.dto.SalesReportRequest;
import com.DTISE.ShelfMasterBE.infrastructure.report.dto.SalesReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface GetSalesReportUseCase {

    List<SalesReportResponse> getSalesReport(SalesReportRequest req);

    Page<SalesReportResponse> getSalesReportPreview(
            Pageable pageable, Long productId, Long categoryId,
            LocalDate startDate, LocalDate endDate, Long warehouseId
    );

}
