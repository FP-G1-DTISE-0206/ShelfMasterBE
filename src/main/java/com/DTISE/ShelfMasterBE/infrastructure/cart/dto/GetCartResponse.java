package com.DTISE.ShelfMasterBE.infrastructure.cart.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCartResponse {

    private Long userId;
    private List<CartItemResponse> cartItems;
    private Long totalQuantity;
    private BigDecimal totalPrice;
    private BigDecimal totalWeight;

}
