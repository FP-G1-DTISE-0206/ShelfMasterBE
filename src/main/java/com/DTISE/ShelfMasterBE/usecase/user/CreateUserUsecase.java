package com.DTISE.ShelfMasterBE.usecase.user;

import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.RegisterRequest;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.RegisterResponse;

public interface CreateUserUsecase {
    RegisterResponse createUser(RegisterRequest req);
}
