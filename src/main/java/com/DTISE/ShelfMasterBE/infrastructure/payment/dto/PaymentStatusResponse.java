package com.DTISE.ShelfMasterBE.infrastructure.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusResponse {
    private String transactionId;
    private String status;
    private String amount;
    private String transactionTime;

    public PaymentStatusResponse(String transactionId, String status) {
    }
}
