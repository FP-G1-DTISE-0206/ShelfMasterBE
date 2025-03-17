package com.DTISE.ShelfMasterBE.usecase.payment;

import com.DTISE.ShelfMasterBE.infrastructure.order.dto.CreateOrderRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.CreatePaymentRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.CreatePaymentResponse;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentResponse;

public interface CreatePaymentUsecase {
//    PaymentResponse execute(PaymentRequest request);
CreatePaymentResponse execute(CreatePaymentRequest request, String email);
}
