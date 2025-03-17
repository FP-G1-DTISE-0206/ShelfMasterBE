package com.DTISE.ShelfMasterBE.infrastructure.order.dto;

import com.DTISE.ShelfMasterBE.entity.OrderItem;
import com.DTISE.ShelfMasterBE.entity.Warehouse;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.GetProductResponse;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private Long id;
    private ProductOrderResponse product;
    private Long quantity;
    private BigDecimal totalPrice;

    public OrderItemResponse(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.product = new ProductOrderResponse(orderItem.getProduct());
        this.quantity = orderItem.getQuantity();
        this.totalPrice = orderItem.getTotalPrice();
    }
}
