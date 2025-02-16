package com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto;

import com.DTISE.ShelfMasterBE.entity.ProductMutation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternalProductMutationRequest {
    private Long productId;
    private Long warehouseOriginId;
    private Long warehouseDestinationId;
    private Integer quantity;

    public ProductMutation toEntity() {
        ProductMutation mutation = new ProductMutation();
        mutation.setProductId(productId);
        mutation.setOriginId(warehouseOriginId);
        mutation.setDestinationId(warehouseDestinationId);
        mutation.setQuantity(quantity);
        return mutation;
    }
}
