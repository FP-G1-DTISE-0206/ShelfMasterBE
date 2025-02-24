package com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto;

import com.DTISE.ShelfMasterBE.entity.ProductMutation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductStockRequest {
    private Long productId;
    private Long warehouseId;
    private Long vendorId;
    private Long quantity;

    public ProductMutation toEntity() {
        ProductMutation mutation = new ProductMutation();
        mutation.setOriginId(vendorId);
        mutation.setDestinationId(warehouseId);
        mutation.setQuantity(quantity);
        return mutation;
    }
}
