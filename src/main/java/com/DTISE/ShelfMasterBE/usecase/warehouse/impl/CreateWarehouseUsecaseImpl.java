package com.DTISE.ShelfMasterBE.usecase.warehouse.impl;

import com.DTISE.ShelfMasterBE.entity.Warehouse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.CreateWarehouseRequest;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseResponse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.repository.WarehouseRepository;
import com.DTISE.ShelfMasterBE.usecase.warehouse.CreateWarehouseUsecase;
import org.springframework.stereotype.Service;

@Service
public class CreateWarehouseUsecaseImpl implements CreateWarehouseUsecase {
    private final WarehouseRepository warehouseRepository;

    public CreateWarehouseUsecaseImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public WarehouseResponse createWarehouse(CreateWarehouseRequest req) {
        try {
            return mapCreatedWarehouse(warehouseRepository.save(req.toEntity()));
        } catch (Exception e) {
            throw new RuntimeException("Can't save warehouse, " + e.getMessage());
        }

    }

    public WarehouseResponse mapCreatedWarehouse(Warehouse createdWarehouse) {
        return new WarehouseResponse(
                createdWarehouse.getId(),
                createdWarehouse.getName(),
                createdWarehouse.getLocation()
        );
    }
}
