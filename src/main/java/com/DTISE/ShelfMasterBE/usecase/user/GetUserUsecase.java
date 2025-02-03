package com.DTISE.ShelfMasterBE.usecase.user;

import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;

public interface GetUserUsecase {
    UserResponse getUser(String email);
}
