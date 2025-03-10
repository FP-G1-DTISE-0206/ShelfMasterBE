package com.DTISE.ShelfMasterBE.infrastructure.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderResponse {
    private Long orderId;
    private String latestStatus;
    private String paymentMethod;
    private Boolean isPaid;
    private BigDecimal totalPrice;
    private Long addressId;
}
