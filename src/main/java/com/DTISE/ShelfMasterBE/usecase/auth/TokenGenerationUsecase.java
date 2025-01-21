package com.DTISE.ShelfMasterBE.usecase.auth;

import com.DTISE.ShelfMasterBE.usecase.auth.impl.TokenGenerationUsecaseImpl;
import org.springframework.security.core.Authentication;

public interface TokenGenerationUsecase {
    enum TokenType {
        ACCESS, REFRESH
    }
    String generateToken(Authentication authentication, TokenType tokenType);
    String refreshAccessToken(String refreshToken);
}
