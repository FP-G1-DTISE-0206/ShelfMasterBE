package com.DTISE.ShelfMasterBE.usecase.userAddress;

import com.DTISE.ShelfMasterBE.infrastructure.userAddress.dto.UserAddressRequest;
import com.DTISE.ShelfMasterBE.infrastructure.userAddress.dto.UserAddressResponse;

public interface CreateUserAddressUsecase {
    UserAddressResponse createUserAddress(UserAddressRequest reql,String email);
}
