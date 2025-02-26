package com.DTISE.ShelfMasterBE.usecase.productMutation;

import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.RejectionReasonRequest;

public interface RejectOrCancelProductMutationUseCase {
    Long cancelProductMutation(Long productMutationId);
    Long rejectProductMutation(Long productMutationId, RejectionReasonRequest req);
}
