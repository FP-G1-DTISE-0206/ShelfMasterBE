package com.DTISE.ShelfMasterBE.infrastructure.admin.controller;

import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.usecase.admin.GetAdminsUsecase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
public class AdminController {
    private final GetAdminsUsecase getAdminsUsecase;

    public AdminController(GetAdminsUsecase getAdminsUsecase) {
        this.getAdminsUsecase = getAdminsUsecase;
    }

    @GetMapping()
    public ResponseEntity<?> getAdmins(@RequestParam int start,
                                       @RequestParam int length,
                                       @RequestParam(required = false) String search) {
        int page = start / length;
        Sort.Direction direction = Sort.Direction.fromString("desc");
        Sort sort = Sort.by(direction, "id");
        Pageable pageable = PageRequest.of(page, length, sort);
        Page<User> adminPage = getAdminsUsecase.getAdmins(pageable, search);
        Map<String, Object> response = new HashMap<>();
        response.put("recordsFiltered", adminPage.getTotalElements());
        response.put("data", adminPage.getContent());
        return ApiResponse.successfulResponse("Admin list retrieved successfully", response);
    }
}
