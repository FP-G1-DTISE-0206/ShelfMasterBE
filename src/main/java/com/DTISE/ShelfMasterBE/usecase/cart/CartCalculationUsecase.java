package com.DTISE.ShelfMasterBE.usecase.cart;

import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CartCalculationRequest;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CartRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentResponse;

public interface CartCalculationUsecase {
    PaymentResponse execute(CartRequest request);
}