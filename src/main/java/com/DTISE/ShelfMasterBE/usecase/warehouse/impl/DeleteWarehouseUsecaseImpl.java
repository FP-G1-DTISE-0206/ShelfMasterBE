package com.DTISE.ShelfMasterBE.usecase.warehouse.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.entity.Warehouse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.repository.WarehouseRepository;
import com.DTISE.ShelfMasterBE.usecase.warehouse.DeleteWarehouseUsecase;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class DeleteWarehouseUsecaseImpl implements DeleteWarehouseUsecase {
    private final WarehouseRepository warehouseRepository;

    public DeleteWarehouseUsecaseImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public void deleteWarehouse(Long id) {
        warehouseRepository
                .findById(id)
                .map(existingWarehouse -> {
                    existingWarehouse.setDeletedAt(OffsetDateTime.now());
                    return warehouseRepository.save(existingWarehouse);
                })
                .orElseThrow(() -> new DataNotFoundException("There's no product with ID: " + id));
    }

}
