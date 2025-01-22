package com.DTISE.ShelfMasterBE.usecase.auth;

import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.LoginResponse;

public interface TokenRefreshUsecase {
    LoginResponse refreshAccessToken(String refreshToken);
}
