package com.DTISE.ShelfMasterBE.infrastructure.cart.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCartResponse {

    private Long userId;
    private List<CartItemResponse> cartItems;
}
