package com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWarehouseRequest {
    @NotNull
    private String name;
    @NotNull
    private String location;
}
