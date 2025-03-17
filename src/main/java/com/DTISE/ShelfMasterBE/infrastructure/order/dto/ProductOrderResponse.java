package com.DTISE.ShelfMasterBE.infrastructure.order.dto;

import com.DTISE.ShelfMasterBE.entity.OrderItem;
import com.DTISE.ShelfMasterBE.entity.Product;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.GetProductResponse;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderResponse {
    private Long id;
    private String name;
    private String sku;
    private BigDecimal price;

    public ProductOrderResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.sku = product.getSku();
        this.price = product.getPrice();
    }
}

