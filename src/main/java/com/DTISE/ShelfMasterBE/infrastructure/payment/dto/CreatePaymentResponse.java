package com.DTISE.ShelfMasterBE.infrastructure.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentResponse {
    private Long orderId;
    private String paymentMethod;
    private String transactionStatus;
    private String redirectUrl;
    private String manualTransferProofUrl;
}
