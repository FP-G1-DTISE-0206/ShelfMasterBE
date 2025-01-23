package com.DTISE.ShelfMasterBE.usecase.warehouse;

import com.DTISE.ShelfMasterBE.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetWarehousesUsecase {
    Page<Warehouse> getWarehouses(Pageable pageable, String search);
}
