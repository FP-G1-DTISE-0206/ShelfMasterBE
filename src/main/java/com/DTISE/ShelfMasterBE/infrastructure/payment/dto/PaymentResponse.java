package com.DTISE.ShelfMasterBE.infrastructure.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String orderId;
    private String transactionToken;
//    private String redirectUrl;
    private BigDecimal totalAmount;
}
