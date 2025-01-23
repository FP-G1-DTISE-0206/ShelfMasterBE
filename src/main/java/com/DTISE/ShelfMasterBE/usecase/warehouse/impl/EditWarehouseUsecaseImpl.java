package com.DTISE.ShelfMasterBE.usecase.warehouse.impl;

import com.DTISE.ShelfMasterBE.entity.Warehouse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.CreateWarehouseRequest;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.repository.WarehouseRepository;
import com.DTISE.ShelfMasterBE.usecase.warehouse.EditWarehouseUsecase;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EditWarehouseUsecaseImpl implements EditWarehouseUsecase {
    private final WarehouseRepository warehouseRepository;

    public EditWarehouseUsecaseImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public Warehouse editWarehouse(CreateWarehouseRequest req, Long id) {
        try {
            Optional<Warehouse> warehouse = warehouseRepository.findById(id);
            if (warehouse.isEmpty()) {
                throw new RuntimeException("Warehouse not found");
            }
            warehouse.get().setName(req.getName());
            warehouse.get().setLocation(req.getLocation());
            return warehouseRepository.save(warehouse.get());
        } catch (Exception e) {
            throw new RuntimeException("Can't update warehouse, " + e.getMessage());
        }
    }
}
