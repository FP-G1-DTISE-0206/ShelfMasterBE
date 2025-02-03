package com.DTISE.ShelfMasterBE.usecase.warehouse.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.entity.Warehouse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.UpdateWarehouseRequest;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseResponse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.repository.WarehouseRepository;
import com.DTISE.ShelfMasterBE.usecase.warehouse.UpdateWarehouseUsecase;
import org.springframework.stereotype.Service;

@Service
public class UpdateWarehouseUsecaseImpl implements UpdateWarehouseUsecase {
    private final WarehouseRepository warehouseRepository;

    public UpdateWarehouseUsecaseImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public WarehouseResponse updateWarehouse(UpdateWarehouseRequest req, Long id) {
        try {
            return warehouseRepository.findById(id)
                    .map(existingWarehouse -> {
                        existingWarehouse.setName(req.getName());
                        existingWarehouse.setLocation(req.getLocation());
                        return warehouseRepository.save(existingWarehouse);
                    })
                    .map(this::mapUpdateWarehouseResponse)
                    .orElseThrow(() -> new DataNotFoundException(
                            "There's no warehouse with ID: " + id));
        } catch (Exception e) {
            throw new RuntimeException("Can't update warehouse, " + e.getMessage());
        }
    }

    private WarehouseResponse mapUpdateWarehouseResponse(Warehouse updatedWarehouse) {
        return new WarehouseResponse(
                updatedWarehouse.getId(),
                updatedWarehouse.getName(),
                updatedWarehouse.getLocation());
    }

}
