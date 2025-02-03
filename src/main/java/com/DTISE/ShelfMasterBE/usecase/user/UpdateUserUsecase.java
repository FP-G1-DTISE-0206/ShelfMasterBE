package com.DTISE.ShelfMasterBE.usecase.user;

import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UpdateUserRequest;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;

public interface UpdateUserUsecase {
    UserResponse updateUser(UpdateUserRequest req,String email);
}
