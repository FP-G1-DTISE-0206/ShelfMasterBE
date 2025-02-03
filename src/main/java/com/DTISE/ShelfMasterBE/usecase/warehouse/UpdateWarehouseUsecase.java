package com.DTISE.ShelfMasterBE.usecase.warehouse;

import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.UpdateWarehouseRequest;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseResponse;

public interface UpdateWarehouseUsecase {
    WarehouseResponse updateWarehouse(UpdateWarehouseRequest req, Long id);
}
