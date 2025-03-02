package com.DTISE.ShelfMasterBE.infrastructure.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockReportRequest {
    private Long productId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long warehouseId;
}
