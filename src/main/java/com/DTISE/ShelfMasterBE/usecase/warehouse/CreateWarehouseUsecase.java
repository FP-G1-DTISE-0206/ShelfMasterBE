package com.DTISE.ShelfMasterBE.usecase.warehouse;

import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseFullResponse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseRequest;

public interface CreateWarehouseUsecase {
    WarehouseFullResponse createWarehouse(WarehouseRequest req);
}
