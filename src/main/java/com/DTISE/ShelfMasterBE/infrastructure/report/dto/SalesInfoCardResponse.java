package com.DTISE.ShelfMasterBE.infrastructure.report.dto;

import java.math.BigDecimal;

public record SalesInfoCardResponse(
        BigDecimal income,
        BigDecimal percentage
) {}
