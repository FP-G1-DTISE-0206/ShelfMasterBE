package com.DTISE.ShelfMasterBE.infrastructure.report.dto;

public record StockReportResponse(
        Long id,
        String originName,
        String destinationName,
        Long productId,
        String productName,
        Long quantity,
        Long absQuantity,
        String requesterName,
        String processorName,
        String remark
) {}
