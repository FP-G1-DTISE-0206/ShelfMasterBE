package com.DTISE.ShelfMasterBE.usecase.warehouse;

import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetWarehousesUsecase {
    Page<WarehouseResponse> getWarehouses(Pageable pageable, String search);
}
