package com.DTISE.ShelfMasterBE.usecase.productMutation;

import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.AutoMutationRequest;

public interface AutoMutationUseCase {
    Long autoMutateAll(AutoMutationRequest request);
}
