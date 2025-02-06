package com.DTISE.ShelfMasterBE.usecase.auth;


import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.SetupAccountRequest;

public interface SetupAccountUsecase {
    void setupAccount(SetupAccountRequest req);
}
