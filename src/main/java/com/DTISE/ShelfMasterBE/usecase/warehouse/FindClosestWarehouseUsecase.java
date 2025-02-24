package com.DTISE.ShelfMasterBE.usecase.warehouse;

import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseFullResponse;

public interface FindClosestWarehouseUsecase {
    WarehouseFullResponse findClosestWarehouse(String email,Long userAddressId);
}
