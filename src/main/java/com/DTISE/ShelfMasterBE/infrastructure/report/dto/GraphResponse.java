package com.DTISE.ShelfMasterBE.infrastructure.report.dto;

import java.math.BigDecimal;

public record GraphResponse(
        String x,
        BigDecimal y
) {}
