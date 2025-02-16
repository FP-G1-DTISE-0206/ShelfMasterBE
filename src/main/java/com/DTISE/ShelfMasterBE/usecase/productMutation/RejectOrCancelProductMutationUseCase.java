package com.DTISE.ShelfMasterBE.usecase.productMutation;

import com.DTISE.ShelfMasterBE.entity.User;

public interface RejectOrCancelProductMutationUseCase {
    Long cancelProductMutation(User user, Long productMutationId);
    Long rejectProductMutation(User user, Long productMutationId);
}
