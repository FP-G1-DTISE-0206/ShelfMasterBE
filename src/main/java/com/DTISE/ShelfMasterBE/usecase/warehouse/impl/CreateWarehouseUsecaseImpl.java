package com.DTISE.ShelfMasterBE.usecase.warehouse.impl;

import com.DTISE.ShelfMasterBE.entity.Warehouse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.CreateWarehouseRequest;
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
    public Warehouse createWarehouse(CreateWarehouseRequest req) {
        try {
            Warehouse newWarehouse = req.toEntity();
            return warehouseRepository.save(newWarehouse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create warehouse, " + e.getMessage());
        }

    }
}
