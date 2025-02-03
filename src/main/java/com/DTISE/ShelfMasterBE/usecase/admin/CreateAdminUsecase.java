package com.DTISE.ShelfMasterBE.usecase.admin;

import com.DTISE.ShelfMasterBE.infrastructure.admin.dto.AdminResponse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.AdminRegisterRequest;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.RegisterResponse;

public interface CreateAdminUsecase {
    AdminResponse createAdmin(AdminRegisterRequest req);
}
