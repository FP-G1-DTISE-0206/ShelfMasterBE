package com.DTISE.ShelfMasterBE.usecase.admin;

import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.AdminRegisterRequest;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;

public interface CreateAdminUsecase {
    UserResponse createAdmin(AdminRegisterRequest req);
}
