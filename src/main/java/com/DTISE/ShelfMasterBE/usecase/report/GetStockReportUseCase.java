package com.DTISE.ShelfMasterBE.usecase.report;

import com.DTISE.ShelfMasterBE.infrastructure.report.dto.StockReportRequest;
import com.DTISE.ShelfMasterBE.infrastructure.report.dto.StockReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface GetStockReportUseCase {

    List<StockReportResponse> getStockReport(StockReportRequest req);

    Page<StockReportResponse> getStockReportPreview(
            Pageable pageable, Long productId, LocalDate startDate,
            LocalDate endDate, Long warehouseId
    );

}
