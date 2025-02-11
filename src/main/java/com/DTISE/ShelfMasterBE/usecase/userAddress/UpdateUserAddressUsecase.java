package com.DTISE.ShelfMasterBE.usecase.userAddress;

import com.DTISE.ShelfMasterBE.infrastructure.userAddress.dto.UserAddressRequest;
import com.DTISE.ShelfMasterBE.infrastructure.userAddress.dto.UserAddressResponse;

public interface UpdateUserAddressUsecase {
     UserAddressResponse updateUserAddress(Long id, UserAddressRequest req, String email);
}
