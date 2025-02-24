package com.DTISE.ShelfMasterBE.usecase.productMutation;

import com.DTISE.ShelfMasterBE.entity.User;

public interface RejectOrCancelProductMutationUseCase {
    Long cancelProductMutation(Long productMutationId);
    Long rejectProductMutation(Long productMutationId);
}
