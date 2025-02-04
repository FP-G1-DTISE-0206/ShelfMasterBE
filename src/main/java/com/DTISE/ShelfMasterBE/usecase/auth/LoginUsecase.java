package com.DTISE.ShelfMasterBE.usecase.auth;

import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.LoginRequest;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.LoginResponse;

public interface LoginUsecase {
    LoginResponse authenticateUser(LoginRequest req);
    LoginResponse authenticateWithGoogle(String googleToken);
}
