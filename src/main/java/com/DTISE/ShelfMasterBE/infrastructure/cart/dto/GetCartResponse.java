package com.DTISE.ShelfMasterBE.infrastructure.cart.dto;


import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class GetCartResponse {

    private BigInteger userId;
    private List<CreateCartItemResponse> cartItems;
}
