package com.DTISE.ShelfMasterBE.infrastructure.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigInteger;

@Data
// DTO untuk memperbarui jumlah item di cart
public class UpdateCartItemRequest {

    @NotNull(message = "Cart ID cannot be null")
    private BigInteger cartId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity must be 0 or more")
    private Integer quantity;
}
