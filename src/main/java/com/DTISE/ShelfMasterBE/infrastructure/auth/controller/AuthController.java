package com.DTISE.ShelfMasterBE.infrastructure.auth.controller;

import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.*;
import com.DTISE.ShelfMasterBE.usecase.admin.CreateAdminUsecase;
import com.DTISE.ShelfMasterBE.usecase.auth.*;
import com.DTISE.ShelfMasterBE.usecase.user.ChangePasswordUsecase;
import com.DTISE.ShelfMasterBE.usecase.user.CreateUserUsecase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final LoginUsecase loginUsecase;
    private final CreateUserUsecase createUserUsecase;
    private final LogoutUsecase logoutUsecase;
    private final TokenRefreshUsecase tokenRefreshUsecase;
    private final CheckPasswordUsecase checkPasswordUsecase;
    private final ChangePasswordUsecase changePasswordUsecase;
    private final CreateAdminUsecase createAdminUsecase;
    private final GoogleAuthUsecase googleAuthUsecase;
    private final VerifyEmailUsecase verifyEmailUsecase;
    private final SetupAccountUsecase setupAccountUsecase;
    private final ResetPasswordUsecase resetPasswordUsecase;
    private final SetupPasswordUsecase setupPasswordUsecase;

    public AuthController(LoginUsecase loginUsecase, CreateUserUsecase createUserUsecase, LogoutUsecase logoutUsecase, TokenRefreshUsecase tokenRefreshUsecase, CheckPasswordUsecase checkPasswordUsecase, ChangePasswordUsecase changePasswordUsecase, CreateAdminUsecase createAdminUsecase, GoogleAuthUsecase googleAuthUsecase, VerifyEmailUsecase verifyEmailUsecase, SetupAccountUsecase setupAccountUsecase, ResetPasswordUsecase resetPasswordUsecase, SetupPasswordUsecase setupPasswordUsecase) {
        this.loginUsecase = loginUsecase;
        this.createUserUsecase = createUserUsecase;
        this.logoutUsecase = logoutUsecase;
        this.tokenRefreshUsecase = tokenRefreshUsecase;
        this.checkPasswordUsecase = checkPasswordUsecase;
        this.changePasswordUsecase = changePasswordUsecase;
        this.createAdminUsecase = createAdminUsecase;
        this.googleAuthUsecase = googleAuthUsecase;
        this.verifyEmailUsecase = verifyEmailUsecase;
        this.setupAccountUsecase = setupAccountUsecase;
        this.resetPasswordUsecase = resetPasswordUsecase;
        this.setupPasswordUsecase = setupPasswordUsecase;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest req) {
        return ApiResponse.successfulResponse("Login successful", loginUsecase.authenticateUser(req));
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest req) {
        var result = createUserUsecase.createUser(req);
        return ApiResponse.successfulResponse("Create new user success, please check your email for verification.", result);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Validated @RequestBody LogoutRequest req) {
        var accessToken = Claims.getJwtTokenString();
        req.setAccessToken(accessToken);
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

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest req) {
        String email = Claims.getEmailFromJwt();
        boolean isPasswordCorrect = checkPasswordUsecase.checkPassword(email, req.getOldPassword());
        if (!isPasswordCorrect) {
            return ApiResponse.failedResponse("Old password is incorrect");
        }
        boolean result = changePasswordUsecase.changePassword(req, email);
        if (!result) {
            return ApiResponse.failedResponse("Failed to change password");
        }
        return ApiResponse.successfulResponse("Password changed successfully");
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> requestBody) {
        log.info(" Google login request received: " + requestBody.toString());
        String googleToken = requestBody.get("token");
        return ApiResponse.successfulResponse("Login successful", loginUsecase.authenticateWithGoogle(googleToken));
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        verifyEmailUsecase.verifyUser(token);
        return ApiResponse.successfulResponse("Email verified successfully! You can now set up your password.");
    }

    @PostMapping("/setup-account")
    public ResponseEntity<?> setupAccount(@RequestBody @Validated SetupAccountRequest req) {
        setupAccountUsecase.setupAccount(req);
        return ApiResponse.successfulResponse("Password setup successful! You can now log in.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword() {
        String email = Claims.getEmailFromJwt();
        resetPasswordUsecase.resetPassword(email);
        return ApiResponse.successfulResponse("Please check your email for the reset link.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Validated RegisterRequest req) {
        resetPasswordUsecase.resetPassword(req.getEmail());
        return ApiResponse.successfulResponse("Please check your email for the reset link.");
    }

    @PostMapping("/setup-password")
    public ResponseEntity<?> setupForgottenPassword(@RequestBody @Validated SetupPasswordRequest req) {
        setupPasswordUsecase.setupPassword(req);
        return ApiResponse.successfulResponse("Password changed successfully");
    }
}
