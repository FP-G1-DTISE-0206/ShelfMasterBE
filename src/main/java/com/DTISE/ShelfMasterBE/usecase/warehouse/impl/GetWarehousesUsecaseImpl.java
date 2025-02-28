package com.DTISE.ShelfMasterBE.usecase.warehouse.impl;


import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseFullResponse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseResponse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.repository.WarehouseRepository;
import com.DTISE.ShelfMasterBE.usecase.warehouse.GetWarehousesUsecase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class GetWarehousesUsecaseImpl implements GetWarehousesUsecase {
    private final WarehouseRepository warehouseRepository;

    public GetWarehousesUsecaseImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public Page<WarehouseResponse> getWarehouses(Pageable pageable, String search) {
        return warehouseRepository.findWarehousesBySearch(search, pageable)
                .map(WarehouseResponse::new);
    }

    @Override
    public WarehouseFullResponse getWarehouse(Long warehouseId) {
        return warehouseRepository.findById(warehouseId).map(WarehouseFullResponse::new).orElseThrow( () -> new DataNotFoundException("There's no warehouse with ID: " + warehouseId));
    }
}
