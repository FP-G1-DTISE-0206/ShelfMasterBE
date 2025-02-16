package com.DTISE.ShelfMasterBE.usecase.productMutation;

import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.AutoMutationRequest;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.AutoMutationResponse;

public interface AutoMutationUseCase {
    AutoMutationResponse autoMutate(AutoMutationRequest request);
}
