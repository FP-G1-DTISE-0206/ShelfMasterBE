package com.DTISE.ShelfMasterBE.usecase.warehouse;

import com.DTISE.ShelfMasterBE.entity.Warehouse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.CreateWarehouseRequest;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseResponse;

public interface CreateWarehouseUsecase {
    WarehouseResponse createWarehouse(CreateWarehouseRequest req);
}
