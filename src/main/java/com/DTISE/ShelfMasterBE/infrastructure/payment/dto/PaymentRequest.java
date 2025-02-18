package com.DTISE.ShelfMasterBE.infrastructure.payment.dto;


import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CartItemRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    private String orderId;
    private List<CartItemRequest> cartItems; // <productId, quantity>
    private BigDecimal amount;
}
