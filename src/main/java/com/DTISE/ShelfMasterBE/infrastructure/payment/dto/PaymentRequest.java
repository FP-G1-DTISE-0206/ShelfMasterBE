package com.DTISE.ShelfMasterBE.infrastructure.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    @NotNull(message = "Order ID is required")
    private String orderId;

    @NotNull(message = "Total Amount is required")
    @Min(value = 1000, message = "Total Amount must be at least 1000")
    private BigDecimal totalAmount;

}
