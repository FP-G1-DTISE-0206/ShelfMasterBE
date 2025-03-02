package com.DTISE.ShelfMasterBE.infrastructure.report.dto;

public record PopularProductResponse(
        Long id,
        String name,
        Long quantity
) {}
