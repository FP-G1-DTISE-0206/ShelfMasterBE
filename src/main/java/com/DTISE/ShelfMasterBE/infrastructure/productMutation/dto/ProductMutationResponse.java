package com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;

public record ProductMutationResponse (
    Long id,
    MutationEntityType originType,
    MutationEntityType destinationType,
    Long originId,
    Long destinationId,
    Long productId,
    String productName,
    Long requesterId,
    String requesterName,
    Long processorId,
    String processorName,
    Boolean isApproved
){}
