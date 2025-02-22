package com.DTISE.ShelfMasterBE.usecase.user;

import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetUserUsecase {
    UserResponse getUser(String email);
    Page<UserResponse> getUsers(Pageable pageable, String search);
}
