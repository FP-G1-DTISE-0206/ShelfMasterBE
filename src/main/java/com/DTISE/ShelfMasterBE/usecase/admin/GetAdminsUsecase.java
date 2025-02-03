package com.DTISE.ShelfMasterBE.usecase.admin;

import com.DTISE.ShelfMasterBE.infrastructure.admin.dto.AdminResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface GetAdminsUsecase {
    Page<AdminResponse> getAdmins(Pageable pageable, String search);
}
