package com.DTISE.ShelfMasterBE.infrastructure.order.dto;

import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderResponse {
    private Long orderId;
    private UserResponse user;
    private String latestStatus;
    private String paymentMethod;
    private WarehouseResponse warehouse;
    private String midtransTokenUrl;
    private String manualTransferProof;
    private BigDecimal totalPrice;
    private BigDecimal finalPrice;
    private Boolean isPaid;
    private Long addressId;
    private List<OrderItemResponse> orderItem;
    private Double shippingCost;
    private String shippingMethod;
}
