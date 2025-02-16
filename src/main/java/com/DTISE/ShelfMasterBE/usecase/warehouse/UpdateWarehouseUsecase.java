package com.DTISE.ShelfMasterBE.usecase.warehouse;

import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseFullResponse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseRequest;

public interface UpdateWarehouseUsecase {
    WarehouseFullResponse updateWarehouse(WarehouseRequest req, Long id);
}
