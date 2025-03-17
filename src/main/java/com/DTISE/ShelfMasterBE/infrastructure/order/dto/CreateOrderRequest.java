package com.DTISE.ShelfMasterBE.infrastructure.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private Long orderId;
    private Long userId;
    private Long addressId;
    private Long paymentMethodId;
    private String manualTransferProof;
    private Long warehouseId;
    private Double shippingCost; 
    private String shippingMethod;
//    private BigDecimal totalPrice;
}
