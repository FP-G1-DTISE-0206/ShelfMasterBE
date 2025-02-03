package com.DTISE.ShelfMasterBE.infrastructure.user.controller;

import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UpdateUserRequest;
import com.DTISE.ShelfMasterBE.usecase.user.GetUserUsecase;
import com.DTISE.ShelfMasterBE.usecase.user.UpdateUserUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final GetUserUsecase getUserUsecase;
    private final UpdateUserUsecase updateUserUsecase;

    public UserController(GetUserUsecase getUserUsecase, UpdateUserUsecase updateUserUsecase) {
        this.getUserUsecase = getUserUsecase;
        this.updateUserUsecase = updateUserUsecase;
    }

    @GetMapping()
    public ResponseEntity<?> getUser() {
        String email = Claims.getEmailFromJwt();
        return ApiResponse.successfulResponse("User details retrieved successfully",getUserUsecase.getUser(email));
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody @Validated UpdateUserRequest req) {
        String email = Claims.getEmailFromJwt();
        return ApiResponse.successfulResponse("User updated successfully",updateUserUsecase.updateUser(req, email));
    }
}
