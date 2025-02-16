package com.DTISE.ShelfMasterBE.usecase.admin;

import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface GetAdminsUsecase {
    Page<UserResponse> getAdmins(Pageable pageable, String search);
    List<UserResponse> getAdminsSearch(String search);
}
