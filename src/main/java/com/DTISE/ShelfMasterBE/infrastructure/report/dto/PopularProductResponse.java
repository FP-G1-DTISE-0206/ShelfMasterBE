package com.DTISE.ShelfMasterBE.infrastructure.report.dto;

import java.math.BigDecimal;

public record PopularProductResponse(
        Long id,
        String name,
        BigDecimal price,
        Long quantity
) {}
