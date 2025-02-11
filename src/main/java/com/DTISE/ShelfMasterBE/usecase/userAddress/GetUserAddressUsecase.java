package com.DTISE.ShelfMasterBE.usecase.userAddress;

import com.DTISE.ShelfMasterBE.infrastructure.userAddress.dto.UserAddressResponse;

import java.util.List;

public interface GetUserAddressUsecase {
    List<UserAddressResponse> getUserAddress(String email);
    UserAddressResponse getUserAddressById(Long id,String email);
}
