package com.DTISE.ShelfMasterBE.infrastructure.report.controller;

import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import com.DTISE.ShelfMasterBE.common.tools.Pagination;
import com.DTISE.ShelfMasterBE.infrastructure.report.dto.SalesReportRequest;
import com.DTISE.ShelfMasterBE.infrastructure.report.dto.StockReportRequest;
import com.DTISE.ShelfMasterBE.usecase.report.GetDashboardDataUseCase;
import com.DTISE.ShelfMasterBE.usecase.report.GetSalesReportUseCase;
import com.DTISE.ShelfMasterBE.usecase.report.GetStockReportUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/report")
@PreAuthorize("hasAnyAuthority('SCOPE_SUPER_ADMIN', 'SCOPE_WH_ADMIN')")
public class ReportController {
    private final GetDashboardDataUseCase getDashboardDataUseCase;
    private final GetSalesReportUseCase getSalesReportUseCase;
    private final GetStockReportUseCase getStockReportUseCase;

    public ReportController(
            GetDashboardDataUseCase getDashboardDataUseCase,
            GetSalesReportUseCase getSalesReportUseCase,
            GetStockReportUseCase getStockReportUseCase
    ) {
        this.getDashboardDataUseCase = getDashboardDataUseCase;
        this.getSalesReportUseCase = getSalesReportUseCase;
        this.getStockReportUseCase = getStockReportUseCase;
    }

    @GetMapping("/cards")
    public ResponseEntity<?> getSalesCardInfos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date,
            @RequestParam(required = false) Long id
    ) {
        return ApiResponse.successfulResponse(
                "All sales card data retrieved successfully",
                getDashboardDataUseCase.getSalesInfo(date, id)
        );
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getThisMonthPopularProducts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date,
            @RequestParam(required = false) Long id
    ) {
        return ApiResponse.successfulResponse(
                "All popular product retrieved successfully",
                getDashboardDataUseCase.getThisMonthPopularProducts(date, id)
        );
    }

    @GetMapping("/month-graph")
    public ResponseEntity<?> getSalesGraphThisMonth(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date,
            @RequestParam(required = false) Long id
    ) {
        return ApiResponse.successfulResponse(
                "Nodes of graph (month) retrieved successfully",
                getDashboardDataUseCase.getSalesGraphThisMonth(date, id)
        );
    }

    @GetMapping("/year-graph")
    public ResponseEntity<?> getSalesGraph(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date,
            @RequestParam(required = false) Long id
    ) {
        return ApiResponse.successfulResponse(
                "Nodes of graph retrieved successfully",
                getDashboardDataUseCase.getSalesGraphThisYear(date, id)
        );
    }

    @GetMapping("/sales-preview")
    public ResponseEntity<?> getSalesReportPreview(
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "10") Integer length,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long warehouseId
    ) {
        return ApiResponse.successfulResponse(
                "Preview sales report retrieved successfully",
                Pagination.mapResponse(getSalesReportUseCase
                        .getSalesReportPreview(
                                Pagination.createPageable(start, length, field, order),
                                productId,
                                categoryId,
                                startDate,
                                endDate,
                                warehouseId))
        );
    }

    @GetMapping("/stock-preview")
    public ResponseEntity<?> getStockReportPreview(
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "10") Integer length,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long warehouseId
    ) {
        return ApiResponse.successfulResponse(
                "Preview stock report retrieved successfully",
                Pagination.mapResponse(getStockReportUseCase
                        .getStockReportPreview(
                                Pagination.createPageable(start, length, field, order),
                                productId,
                                startDate,
                                endDate,
                                warehouseId))
        );
    }

    @PostMapping("/sales")
    public ResponseEntity<?> getSalesReport(
            @RequestBody @Validated SalesReportRequest req) {
        return ApiResponse.successfulResponse(
                "Sales report retrieved successfully",
                getSalesReportUseCase.getSalesReport(req));
    }

    @PostMapping("/stock")
    public ResponseEntity<?> getStockReport(
            @RequestBody @Validated StockReportRequest req) {
        return ApiResponse.successfulResponse(
                "Stock report retrieved successfully",
                getStockReportUseCase.getStockReport(req));
    }
}
