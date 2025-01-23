package com.DTISE.ShelfMasterBE.usecase.warehouse;

import com.DTISE.ShelfMasterBE.entity.Warehouse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.CreateWarehouseRequest;

public interface EditWarehouseUsecase {
    Warehouse editWarehouse(CreateWarehouseRequest req, Long id);
}
