package com.DTISE.ShelfMasterBE.usecase.productMutation;

import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.InternalProductMutationRequest;

public interface CreateProductMutationUseCase {
    Long createInternalProductMutation(InternalProductMutationRequest req);
}
