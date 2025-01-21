package com.DTISE.ShelfMasterBE.usecase.auth;

import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.LogoutRequest;

public interface LogoutUsecase {
    Boolean logoutUser(LogoutRequest req);
}
