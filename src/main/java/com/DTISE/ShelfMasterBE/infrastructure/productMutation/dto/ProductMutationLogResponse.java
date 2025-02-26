package com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto;

import com.DTISE.ShelfMasterBE.common.enums.MutationStatusEnum;

import java.time.OffsetDateTime;

public record ProductMutationLogResponse(
        Long id,
        MutationStatusEnum status,
        OffsetDateTime createdAt,
        String reason
) {}
