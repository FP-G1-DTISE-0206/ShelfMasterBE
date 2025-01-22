package com.DTISE.ShelfMasterBE.usecase.auth.impl;

import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.LoginResponse;
import com.DTISE.ShelfMasterBE.usecase.auth.TokenGenerationUsecase;
import com.DTISE.ShelfMasterBE.usecase.auth.TokenRefreshUsecase;
import org.springframework.stereotype.Service;

@Service
public class TokenRefreshUsecaseImpl implements TokenRefreshUsecase {
    private final TokenGenerationUsecase tokenService;

    public TokenRefreshUsecaseImpl(TokenGenerationUsecase tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public LoginResponse refreshAccessToken(String refreshToken) {
        String newAccessToken = tokenService.refreshAccessToken(refreshToken);
        return new LoginResponse(newAccessToken, refreshToken, "Bearer");
    }
}
