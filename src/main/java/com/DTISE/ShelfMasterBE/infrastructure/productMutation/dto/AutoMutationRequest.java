package com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoMutationRequest {
    private Long orderId;
    private Long warehouseId;
}
