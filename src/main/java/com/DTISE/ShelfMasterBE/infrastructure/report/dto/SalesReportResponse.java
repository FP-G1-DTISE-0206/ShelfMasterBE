package com.DTISE.ShelfMasterBE.infrastructure.report.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record SalesReportResponse(
        Long mutationOrderId,
        Long orderId,
        Long productId,
        String productName,
        String categoryName,
        Long quantity,
        BigDecimal totalPrice,
        OffsetDateTime createdAt
) {}
