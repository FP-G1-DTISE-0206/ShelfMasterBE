package com.DTISE.ShelfMasterBE.infrastructure.auth.controller;

import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.LoginRequest;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.LogoutRequest;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.RegisterRequest;
import com.DTISE.ShelfMasterBE.usecase.auth.LoginUsecase;
import com.DTISE.ShelfMasterBE.usecase.auth.LogoutUsecase;
import com.DTISE.ShelfMasterBE.usecase.auth.TokenBlacklistUsecase;
import com.DTISE.ShelfMasterBE.usecase.auth.TokenRefreshUsecase;
import com.DTISE.ShelfMasterBE.usecase.user.CreateUserUsecase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final LoginUsecase loginUsecase;
    private final CreateUserUsecase createUserUsecase;
    private final LogoutUsecase logoutUsecase;
    private final TokenRefreshUsecase tokenRefreshUsecase;

    public AuthController(LoginUsecase loginUsecase, CreateUserUsecase createUserUsecase, TokenBlacklistUsecase tokenBlacklistUsecase, LogoutUsecase logoutUsecase, TokenRefreshUsecase tokenRefreshUsecase) {
        this.loginUsecase = loginUsecase;
        this.createUserUsecase = createUserUsecase;
        this.logoutUsecase = logoutUsecase;
        this.tokenRefreshUsecase = tokenRefreshUsecase;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest req) {
        return ApiResponse.successfulResponse("Login successful", loginUsecase.authenticateUser(req));
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest req) {
        var result = createUserUsecase.createUser(req);
        return ApiResponse.successfulResponse("Create new user success", result);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Validated @RequestBody LogoutRequest req) {
        var accessToken = Claims.getJwtTokenString();
        req.setAccessToken(accessToken);
//        log.info("access token: "+req.getAccessToken());
//        log.info("refresh token: "+req.getRefreshToken());
        return ApiResponse.successfulResponse("Logout successful", logoutUsecase.logoutUser(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh() {
        String tokenType = Claims.getTokenTypeFromJwt();
        if (!"REFRESH".equals(tokenType)) {
            return ApiResponse.failedResponse("Invalid refresh token");
        }
        String token = Claims.getJwtTokenString();
        return ApiResponse.successfulResponse("Refresh successful", tokenRefreshUsecase.refreshAccessToken(token));
    }
}
