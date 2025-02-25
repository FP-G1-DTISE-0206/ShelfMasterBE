package com.DTISE.ShelfMasterBE.usecase.cart;

import java.math.BigInteger;

public interface RemoveCartItemUsecase {
//    void execute(BigInteger cartItemId);
    void execute(Long userId, Long cartId);
}
