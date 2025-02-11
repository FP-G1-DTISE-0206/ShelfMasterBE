package com.DTISE.ShelfMasterBE.infrastructure.user.controller;

import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import com.DTISE.ShelfMasterBE.common.tools.Pagination;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UpdateUserRequest;
import com.DTISE.ShelfMasterBE.infrastructure.userAddress.dto.UserAddressRequest;
import com.DTISE.ShelfMasterBE.usecase.user.GetUserUsecase;
import com.DTISE.ShelfMasterBE.usecase.user.UpdateUserUsecase;
import com.DTISE.ShelfMasterBE.usecase.userAddress.CreateUserAddressUsecase;
import com.DTISE.ShelfMasterBE.usecase.userAddress.DeleteUserAddressUsecase;
import com.DTISE.ShelfMasterBE.usecase.userAddress.GetUserAddressUsecase;
import com.DTISE.ShelfMasterBE.usecase.userAddress.UpdateUserAddressUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final GetUserUsecase getUserUsecase;
    private final UpdateUserUsecase updateUserUsecase;
    private final CreateUserAddressUsecase createUserAddressUsecase;
    private final GetUserAddressUsecase getUserAddressUsecase;
    private final UpdateUserAddressUsecase updateUserAddressUsecase;
    private final DeleteUserAddressUsecase deleteUserAddressUsecase;

    public UserController(GetUserUsecase getUserUsecase, UpdateUserUsecase updateUserUsecase, CreateUserAddressUsecase createUserAddressUsecase, GetUserAddressUsecase getUserAddressUsecase, UpdateUserAddressUsecase updateUserAddressUsecase, DeleteUserAddressUsecase deleteUserAddressUsecase) {
        this.getUserUsecase = getUserUsecase;
        this.updateUserUsecase = updateUserUsecase;
        this.createUserAddressUsecase = createUserAddressUsecase;
        this.getUserAddressUsecase = getUserAddressUsecase;
        this.updateUserAddressUsecase = updateUserAddressUsecase;
        this.deleteUserAddressUsecase = deleteUserAddressUsecase;
    }

    @GetMapping()
    public ResponseEntity<?> getUser() {
        String email = Claims.getEmailFromJwt();
        return ApiResponse.successfulResponse("User details retrieved successfully", getUserUsecase.getUser(email));
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody @Validated UpdateUserRequest req) {
        String email = Claims.getEmailFromJwt();
        return ApiResponse.successfulResponse("User updated successfully", updateUserUsecase.updateUser(req, email));
    }

    @GetMapping("/address")
    public ResponseEntity<?> getUserAddress() {
        String email = Claims.getEmailFromJwt();
        return ApiResponse.successfulResponse("User address retrieved successfully", getUserAddressUsecase.getUserAddress(email));
//        return  ApiResponse.successfulResponse("User address retrieved successfully");
    }

    @PostMapping("/address")
    public ResponseEntity<?> createUserAddress(@RequestBody @Validated UserAddressRequest req) {
        String email = Claims.getEmailFromJwt();
        return ApiResponse.successfulResponse("User address created successfully", createUserAddressUsecase.createUserAddress(req, email));
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<?> getUserAddressById(@PathVariable Long id) {
        String email = Claims.getEmailFromJwt();
        return ApiResponse.successfulResponse("User address retrieved successfully", getUserAddressUsecase.getUserAddressById(id, email));
    }

    @PutMapping("/address/{id}")
    public ResponseEntity<?> updateUserAddress(@PathVariable Long id, @RequestBody @Validated UserAddressRequest req) {
        String email = Claims.getEmailFromJwt();
        return ApiResponse.successfulResponse("User address updated successfully", updateUserAddressUsecase.updateUserAddress(id, req, email));
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<?> deleteUserAddress(@PathVariable Long id) {
        String email = Claims.getEmailFromJwt();
        deleteUserAddressUsecase.deleteUserAddress(id, email);
        return ApiResponse.successfulResponse("User address deleted successfully");
    }
}
