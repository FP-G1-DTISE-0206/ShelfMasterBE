package com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindClosestRequest {
    private Long userAddressId;
}
