package com.DTISE.ShelfMasterBE.usecase.auth.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.LoginRequest;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.LoginResponse;
import com.DTISE.ShelfMasterBE.usecase.auth.LoginUsecase;
import com.DTISE.ShelfMasterBE.usecase.auth.TokenGenerationUsecase;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class LoginUsecaseImpl implements LoginUsecase {
    private final long ACCESS_TOKEN_EXPIRY = 900L;
    private final long REFRESH_TOKEN_EXPIRY = 86400L;

    private final AuthenticationManager authenticationManager;
    private final TokenGenerationUsecase tokenService;

    public LoginUsecaseImpl(AuthenticationManager authenticationManager, TokenGenerationUsecase tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    public LoginResponse authenticateUser(LoginRequest req) {
        try {
//            log.info("Loggingin with");
//            log.info(req.getEmail());
//            log.info(req.getPassword());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );
            String accessToken = tokenService.generateToken(authentication, TokenGenerationUsecase.TokenType.ACCESS);
            String refreshToken = tokenService.generateToken(authentication, TokenGenerationUsecase.TokenType.REFRESH);
            return new LoginResponse(accessToken, refreshToken, "Bearer");
        } catch (AuthenticationException e) {
            throw new DataNotFoundException("Wrong credentials");
        }
    }
}
