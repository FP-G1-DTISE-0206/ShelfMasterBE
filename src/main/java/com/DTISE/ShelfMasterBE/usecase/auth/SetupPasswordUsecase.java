package com.DTISE.ShelfMasterBE.usecase.auth;

import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.SetupPasswordRequest;

public interface SetupPasswordUsecase {
    void setupPassword(SetupPasswordRequest req);
}
