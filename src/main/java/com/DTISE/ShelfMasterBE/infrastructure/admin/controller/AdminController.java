package com.DTISE.ShelfMasterBE.infrastructure.admin.controller;

import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import com.DTISE.ShelfMasterBE.common.tools.Pagination;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.admin.dto.ChangeAdminPasswordRequest;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.AdminRegisterRequest;
import com.DTISE.ShelfMasterBE.usecase.admin.ChangeAdminPasswordUsecase;
import com.DTISE.ShelfMasterBE.usecase.admin.CreateAdminUsecase;
import com.DTISE.ShelfMasterBE.usecase.admin.DeleteAdminUsecase;
import com.DTISE.ShelfMasterBE.usecase.admin.GetAdminsUsecase;
import com.DTISE.ShelfMasterBE.usecase.user.GetUserUsecase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
public class AdminController {
    private final GetAdminsUsecase getAdminsUsecase;
    private final CreateAdminUsecase createAdminUsecase;
    private final DeleteAdminUsecase deleteAdminUsecase;
    private final ChangeAdminPasswordUsecase changeAdminPasswordUsecase;
    private final GetUserUsecase getUserUsecase;

    public AdminController(GetAdminsUsecase getAdminsUsecase, CreateAdminUsecase createAdminUsecase, DeleteAdminUsecase deleteAdminUsecase, ChangeAdminPasswordUsecase changeAdminPasswordUsecase, GetUserUsecase getUserUsecase) {
        this.getAdminsUsecase = getAdminsUsecase;
        this.createAdminUsecase = createAdminUsecase;
        this.deleteAdminUsecase = deleteAdminUsecase;
        this.changeAdminPasswordUsecase = changeAdminPasswordUsecase;
        this.getUserUsecase = getUserUsecase;
    }

    @GetMapping()
    public ResponseEntity<?> getAdmins(@RequestParam int start,
                                       @RequestParam int length,
                                       @RequestParam(required = false) String search,
                                       @RequestParam(required = false) String field,
                                       @RequestParam(required = false) String order) {
        return ApiResponse.successfulResponse(
                "Admin list retrieved successfully",
                Pagination.mapResponse(getAdminsUsecase
                        .getAdmins(
                                Pagination.createPageable(start, length, field, order),
                                search))
        );
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@RequestParam int start,
                                       @RequestParam int length,
                                       @RequestParam(required = false) String search,
                                       @RequestParam(required = false) String field,
                                       @RequestParam(required = false) String order) {
        return ApiResponse.successfulResponse(
                "User list retrieved successfully",
                Pagination.mapResponse(getUserUsecase
                        .getUsers(
                                Pagination.createPageable(start, length, field, order),
                                search))
        );
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchAdmins(@RequestParam String search) {
        return ApiResponse.successfulResponse("Admin list retrieved successfully",getAdminsUsecase.getAdminsSearch(search));
    }

    @PostMapping("/register")
    public ResponseEntity<?> adminRegister(@RequestBody @Validated AdminRegisterRequest req) {
        var result = createAdminUsecase.createAdmin(req);
        return ApiResponse.successfulResponse("Create new user success", result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        deleteAdminUsecase.deleteAdmin(id);
        return ApiResponse.successfulResponse("Delete admin successfully");
    }

    @PostMapping("/change-password/{id}")
    public ResponseEntity<?> changeAdminPassword(@PathVariable Long id, @RequestBody ChangeAdminPasswordRequest req) {
        changeAdminPasswordUsecase.changeAdminPassword(id, req.getPassword());
        return ApiResponse.successfulResponse("Password changed successfully");
    }

}
