package com.DTISE.ShelfMasterBE.usecase.payment;

import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.ManualTransferRequest;

public interface UploadManualTransferProofUsecase {
    void execute(ManualTransferRequest request);
}
