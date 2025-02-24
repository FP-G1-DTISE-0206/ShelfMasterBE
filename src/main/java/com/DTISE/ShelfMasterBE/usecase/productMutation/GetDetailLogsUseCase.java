package com.DTISE.ShelfMasterBE.usecase.productMutation;

import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.ProductMutationLogResponse;

import java.util.List;

public interface GetDetailLogsUseCase {
    List<ProductMutationLogResponse> getLogs(Long productMutationId);
}
