package com.DTISE.ShelfMasterBE.usecase.cart;

import java.math.BigInteger;

public interface RemoveCartItemUsecase {
    void execute(BigInteger userId, BigInteger cartItemId);
}
