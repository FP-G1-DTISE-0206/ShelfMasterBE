package com.DTISE.ShelfMasterBE.usecase.productMutation;

import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.MutationTypeResponse;

import java.util.List;

public interface GetMutationTypeUseCase {
    List<MutationTypeResponse> getAllMutationType();
}
