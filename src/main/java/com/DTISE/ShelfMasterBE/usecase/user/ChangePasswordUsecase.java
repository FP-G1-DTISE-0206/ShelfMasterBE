package com.DTISE.ShelfMasterBE.usecase.user;

import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.ChangePasswordRequest;

public interface ChangePasswordUsecase {
    boolean changePassword(ChangePasswordRequest req, String email);
}
