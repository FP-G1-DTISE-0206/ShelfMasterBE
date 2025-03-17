package com.DTISE.ShelfMasterBE.infrastructure.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatusResponse {
    private Long orderId;
    private String paymentMethod;
    private String transactionId;
    private String transactionStatus;
    private String paymentProofUrl;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}


//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class PaymentStatusResponse {
//    private String transactionId;
//    private String status;
//    private String amount;
//    private String transactionTime;
//
//    public PaymentStatusResponse(String transactionId, String status) {
//    }
//}
