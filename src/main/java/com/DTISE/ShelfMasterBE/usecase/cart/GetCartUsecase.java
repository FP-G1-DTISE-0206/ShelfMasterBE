package com.DTISE.ShelfMasterBE.usecase.cart;

import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.GetCartResponse;

import java.math.BigInteger;

public interface GetCartUsecase {
    GetCartResponse execute(String email);
}
